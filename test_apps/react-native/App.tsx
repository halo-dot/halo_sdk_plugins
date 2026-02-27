import React, { useEffect, useRef, useState } from "react";
import {
  ActivityIndicator,
  FlatList,
  PermissionsAndroid,
  Platform,
  StatusBar,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import {
  HaloSdk,
  type HaloAttestationHealthResult,
  type HaloInitializationResult,
  type HaloTransactionResult,
  type HaloUIMessage,
  type IHaloCallbacks,
} from "halo-sdk-react-native";
import { getJwt } from "./src/jwt/JwtToken";
import { Config } from "./src/config";

interface Message {
  id: string;
  text: string;
  color: string;
}

export default function App() {
  const [amount, setAmount] = useState("");
  const [merchantRef, setMerchantRef] = useState("");
  const [messages, setMessages] = useState<Message[]>([]);
  const [isInitializing, setIsInitializing] = useState(true);
  const msgIdRef = useRef(0);

  useEffect(() => {
    requestPermissionsThenInit();
  }, []);

  function addMessage(text: string, color = "black") {
    const id = String(msgIdRef.current++);
    setMessages((prev) => [{ id, text, color }, ...prev]);
  }

  async function requestPermissionsThenInit() {
    if (Platform.OS === "android") {
      const sdkVersion = typeof Platform.Version === "number" ? Platform.Version : parseInt(Platform.Version, 10);
      const permissions: typeof PermissionsAndroid.PERMISSIONS[keyof typeof PermissionsAndroid.PERMISSIONS][] = [PermissionsAndroid.PERMISSIONS.CAMERA];

      const message = `Android SDK version: ${sdkVersion}.`;
      addMessage(message, "blue");
      if (sdkVersion >= 31) {
        // Android 12+
        permissions.push(
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
          PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        );
      } else {
        // Below Android 12: request location (required for Bluetooth LE scanning)
        permissions.push(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
        );
      }

      await PermissionsAndroid.requestMultiple(permissions);
    }
    initializeSdk();
  }

  function initializeSdk() {
    const callbacks: IHaloCallbacks = {
      onAttestationError(details: HaloAttestationHealthResult) {
        console.log("onAttestationError", details);
        setIsInitializing(false);
        addMessage(
          `Attestation error: ${details.resultType} ${details.errorCode}`,
          "red",
        );
      },
      onHaloTransactionResult(result: HaloTransactionResult) {
        console.log("onHaloTransactionResult", result);
        addMessage(
          `Transaction result: ${result.resultType} ${result.errorCode} ${result.errorDetails ?? ""}`,
          "green",
        );
      },
      onHaloUIMessage(message: HaloUIMessage) {
        console.log("onHaloUIMessage", message);
        addMessage(`UI Message: ${message.msgID}`);
      },
      onInitializationResult(result: HaloInitializationResult) {
        console.log("onInitializationResult", result);
        setIsInitializing(false);
        addMessage(`Initialisation result: ${result.resultType}`);
      },
      onRequestJWT(jwtCallback: (jwt: string) => void) {
        console.log("onRequestJWT");
        try {
          const jwt = getJwt();
          jwtCallback(jwt);
        } catch (e: any) {
          setIsInitializing(false);
          addMessage(`JWT error: ${e.message}`, "red");
        }
      },
      onSecurityError(errorCode: string) {
        console.log("onSecurityError", errorCode);
        addMessage(`Security error: ${errorCode}`, "red");
      },
      onCameraControlLost() {
        console.log("onCameraControlLost");
        addMessage("Camera control lost", "red");
      },
    };

    HaloSdk.initialize(
      callbacks,
      Config.applicationPackageName,
      Config.applicationVersion,
      Config.onStartTransactionTimeOut,
      Config.enableSchemeAnimations,
    ).catch((e: any) => {
      setIsInitializing(false);
      addMessage(`SDK initialisation error: ${e.message}`, "red");
    });
  }

  function isValidAmount(a: string): boolean {
    try {
      return a.length > 0 && parseFloat(a) > 0;
    } catch {
      return false;
    }
  }

  async function startTransaction() {
    if (!isValidAmount(amount)) {
      addMessage("Invalid amount, please provide amount", "orange");
      return;
    }
    if (!merchantRef.trim()) {
      addMessage(
        "Invalid merchant reference, please provide merchant reference",
        "orange",
      );
      return;
    }
    try {
      const result = await HaloSdk.startTransaction(
        parseFloat(amount),
        merchantRef,
        "ZAR",
      );
      addMessage(
        `Transaction start state: ${result.resultType} ${result.errorCode}`,
      );
    } catch (e: any) {
      addMessage(`Transaction start error: ${e.message}`, "red");
    }
  }

  async function cardRefundTransaction() {
    if (!isValidAmount(amount)) {
      addMessage("Invalid amount, please provide amount", "orange");
      return;
    }
    if (!merchantRef.trim()) {
      addMessage(
        "Invalid merchant reference, please provide merchant reference",
        "orange",
      );
      return;
    }
    try {
      const result = await HaloSdk.cardRefundTransaction(
        parseFloat(amount),
        merchantRef,
        "ZAR",
      );
      addMessage(
        `Transaction start state: ${result.resultType} ${result.errorCode}`,
      );
    } catch (e: any) {
      addMessage(`Refund start error: ${e.message}`, "red");
    }
  }

  function cancelTransaction() {
    HaloSdk.cancelTransaction();
  }

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#fff" />

      {/* Form */}
      <View style={styles.form}>
        <Text style={styles.label}>Amount (ZAR)</Text>
        <TextInput
          style={styles.amountInput}
          placeholder="0.00"
          placeholderTextColor="#999"
          value={amount}
          onChangeText={setAmount}
          keyboardType="decimal-pad"
          returnKeyType="done"
        />

        <Text style={styles.label}>Merchant Reference</Text>
        <TextInput
          style={styles.input}
          placeholder="e.g MerRef012"
          placeholderTextColor="#999"
          value={merchantRef}
          onChangeText={setMerchantRef}
          returnKeyType="done"
        />
      </View>

      {/* Messages list / initializing spinner */}
      {isInitializing ? (
        <View style={styles.spinnerContainer}>
          <ActivityIndicator size="large" color="#1976D2" />
          <Text style={styles.initText}>Initialising SDK...</Text>
        </View>
      ) : (
        <FlatList
          style={styles.messages}
          data={messages}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <Text style={[styles.messageText, { color: item.color }]}>
              {item.text}
            </Text>
          )}
        />
      )}

      {/* Action buttons */}
      <View style={styles.buttons}>
        <TouchableOpacity style={styles.btnCharge} onPress={startTransaction}>
          <Text style={styles.btnText}>Charge</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.btnCharge}
          onPress={cardRefundTransaction}
        >
          <Text style={styles.btnText}>CP Refund</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btnCancel} onPress={cancelTransaction}>
          <Text style={styles.btnText}>Cancel</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 16,
    paddingVertical: 8,
    backgroundColor: "#fff",
  },
  form: {
    gap: 4,
    paddingVertical: 8,
  },
  label: {
    fontSize: 13,
    color: "#555",
    marginBottom: 2,
    marginTop: 8,
  },
  amountInput: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    paddingHorizontal: 12,
    fontSize: 28,
    fontWeight: "300",
    color: "#000",
    height: 56,
  },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    paddingHorizontal: 12,
    fontSize: 16,
    color: "#000",
    height: 48,
  },
  messages: {
    flex: 1,
    marginVertical: 8,
  },
  spinnerContainer: {
    flex: 1,
    marginVertical: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  initText: {
    marginTop: 8,
    fontSize: 14,
    color: "#666",
  },
  messageText: {
    fontSize: 13,
    paddingVertical: 2,
    color: "#000",
  },
  buttons: {
    gap: 8,
    paddingBottom: 8,
  },
  btnCharge: {
    backgroundColor: "#1976D2",
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: "center",
  },
  btnCancel: {
    backgroundColor: "#9E9E9E",
    paddingVertical: 14,
    borderRadius: 8,
    alignItems: "center",
  },
  btnText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
});
