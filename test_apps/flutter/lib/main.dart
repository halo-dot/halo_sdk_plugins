import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:sdkflutterplugin/model/halo_attestation_health_result.dart';
import 'package:sdkflutterplugin/model/halo_initialization_result.dart';
import 'package:sdkflutterplugin/model/halo_transaction_result.dart';
import 'package:sdkflutterplugin/model/halo_ui_message.dart';
import 'package:sdkflutterplugin/model/i_halo_callbacks.dart';
import 'package:sdkflutterplugin/sdkflutterplugin.dart';
import 'package:sdkflutterplugin_example/jwt_token.dart';

import 'ui/amount_field.dart';
import 'ui/keypad.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  String amount = "";
  List<UiMessage> uiMessages = [];
  var textFieldController = TextEditingController();

  Future<void> initPlatformState() async {
    if (!mounted) return;

    checkPermissions();
    setState(() {
      amount = "";
      uiMessages = [];
    });
  }

  Future<void> checkPermissions() async {
    var permissions = [
      Permission.camera,
      Permission.phone,
      Permission.storage,
      Permission.notification,
      Permission.location,
    ];

    for (var permission in permissions) {
      await requestPermission(permission);
    }

    onInitializeSdk();
  }

  Future<void> requestPermission(Permission permission) async {
    var permissionStatus = await permission.status;
    if (permissionStatus.isGranted) {
      debugPrint("${permission} permission is granted, not requesting");
    } else if (permissionStatus.isPermanentlyDenied) {
      debugPrint(
          "${permission} permission is permanently denied, enable in settings");
    } else {
      var requestResult = await permission.request();

      if (requestResult.isGranted) {
        debugPrint("${permission} permission is granted");
      } else if (requestResult.isPermanentlyDenied) {
        debugPrint(
            "${permission} permission is permanently denied, enable in settings");
      }
    }
  }

  void setAmount(String value) {
    setState(() {
      amount = value;
    });
  }

  void onInitializeSdk() {
    var haloCallbacks = HaloCallbacks(setUiMessage);
    try {
      Sdkflutterplugin.initializeHaloSDK(haloCallbacks,
          "za.co.synthesis.halo.sdkflutterplugin_example", "0.0.1", 300000);
    } on PlatformException catch (e) {
      setUiMessage(UiMessage(
          "SDK initialisation error: ${e.code} ${e.message}", Colors.red));
    }
  }

  void setUiMessage(UiMessage m) {
    setState(() {
      uiMessages.add(m);
    });
  }

  void startTransaction() async {
    if (!isValidAmount(amount)) {
      setUiMessage(
          UiMessage("Invalid amount, please provide amount", Colors.orange));
    } else if (textFieldController.text.isEmpty) {
      setUiMessage(UiMessage(
          "Invalid merchant reference, please provide merchant reference",
          Colors.orange));
    } else {
      try {
        var startTransactionResult = await Sdkflutterplugin.startTransaction(
            double.parse(amount), textFieldController.text, 'ZAR');
        setUiMessage(UiMessage(
            "Transaction start state: ${startTransactionResult.resultType} ${startTransactionResult.errorCode}",
            Colors.black));
      } on PlatformException catch (e) {
        setUiMessage(UiMessage(
            "Transaction start error: ${e.code} ${e.message}", Colors.red));
      }
    }
  }

  void cancelTransaction() {
    Sdkflutterplugin.cancelTransaction();
  }

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    textFieldController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        resizeToAvoidBottomInset: false,
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Padding(
            padding: const EdgeInsets.fromLTRB(8, 2, 10, 8),
            child: Column(mainAxisSize: MainAxisSize.max, children: [
              AmountField(flex: 2, amount: amount),
              Expanded(
                  flex: 1,
                  child: TextField(
                      controller: textFieldController,
                      decoration: InputDecoration(
                          border: OutlineInputBorder(),
                          hintText: 'Merchant reference'))),
              Expanded(
                  flex: 2,
                  child: ListView(
                      reverse: true,
                      children: uiMessages.reversed
                          .map((it) => Text(
                                "${it.text}",
                                softWrap: true,
                                style: TextStyle(color: it.color),
                              ))
                          .toList())),
              Keypad(flex: 6, amount: amount, setAmount: setAmount),
              Column(crossAxisAlignment: CrossAxisAlignment.stretch, children: [
                ElevatedButton(
                  onPressed: startTransaction,
                  child: const Text('Charge',
                      style:
                          TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                ),
                ElevatedButton(
                  onPressed: cancelTransaction,
                  style: ButtonStyle(
                      backgroundColor:
                          MaterialStateProperty.all<Color>(Colors.grey)),
                  child: const Text('Cancel',
                      style:
                          TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                )
              ])
            ])),
      ),
    );
  }
}

bool isValidAmount(String amount) {
  try {
    return amount.isNotEmpty && double.parse(amount) > 0;
  } catch (e) {
    return false;
  }
}

class UiMessage {
  String text;
  Color color;
  UiMessage(this.text, this.color);
}

class HaloCallbacks implements IHaloCallbacks {
  HaloCallbacks(this.setUiMessage);

  final Function setUiMessage;

  @override
  void onAttestationError(HaloAttestationHealthResult details) {
    debugPrint("example app: attestation error: $details");
    setUiMessage(UiMessage(
        "Attestation error: ${details.resultType} ${details.errorCode}",
        Colors.red));
  }

  @override
  void onHaloTransactionResult(HaloTransactionResult result) {
    debugPrint("example app: transaction result: $result");
    setUiMessage(UiMessage(
        'Transaction result: ${result.resultType} ${result.errorCode} ${result.errorDetails}',
        Colors.green));
  }

  @override
  void onHaloUIMessage(HaloUIMessage message) {
    debugPrint("example app: UI message: $message");
    setUiMessage(UiMessage("UI Message: ${message.msgId.name}", Colors.black));
  }

  @override
  void onInitializationResult(HaloInitializationResult result) {
    debugPrint("example app: initialization message: $result");
    setUiMessage(UiMessage(
        "Initialisation result: ${result.resultType?.name}", Colors.black));
  }

  @override
  void onRequestJWT(void Function(String jwt) callback) {
    debugPrint("example app: onRequestJWT");
    var jwt = JwtToken.getJwt();
    debugPrint(jwt);
    callback(jwt);
  }

  @override
  void onSecurityError(errorCode) {
    debugPrint("example app: security error: $errorCode");
    setUiMessage(UiMessage("Security error: $errorCode", Colors.red));
  }
}
