package cordovapluginhalo;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import za.co.synthesis.halo.sdk.HaloSDK;
import za.co.synthesis.halo.sdk.model.AttestationHealthResultType;
import za.co.synthesis.halo.sdk.model.HaloAttestationHealthResult;
import za.co.synthesis.halo.sdk.model.HaloCurrencyValue;
import za.co.synthesis.halo.haloCommonInterface.CardType;
import za.co.synthesis.halo.haloCommonInterface.HaloErrorCode;
import za.co.synthesis.halo.haloCommonInterface.TransactionType;
import za.co.synthesis.halo.sdk.model.HaloInitializationParameters;
import za.co.synthesis.halo.sdk.model.HaloInitializationResult;
import za.co.synthesis.halo.sdk.model.HaloInitializationResultType;
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionReceipt;
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResult;
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResultType;
import za.co.synthesis.halo.sdk.model.HaloUIMessage;
import za.co.synthesis.halo.haloCommonInterface.HaloUIMessageID;
import za.co.synthesis.halo.sdk.model.HaloWarning;

public class HaloPlugin extends CordovaPlugin {
    static String TAG = "HaloPlugin";
    private Function1<? super String, Unit> jwtCallback = null;

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    HaloCallbacks hcb = new HaloCallbacks(this);

    String [] permissions = { Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT };

    CallbackContext callbacks;

    static List<CallbackContext>  callbackContextList = new ArrayList<CallbackContext>();
    static String callbackContextId = null;

    OkHttpClient client = new OkHttpClient();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "registerCallbacks":
                Log.d(TAG, "Registering callbacks");
                callbacks = callbackContext;
                callbackContextList.add(callbacks);

                return true;

            case "initialize":
                boolean cameraPermission = cordova.hasPermission(CAMERA);
                boolean accesscoarselocationPermission = cordova.hasPermission(ACCESS_COARSE_LOCATION);

                String[] bluetoothPermissions = getBluetoothPermissions();

                boolean bluetoothPermission1 = cordova.hasPermission(bluetoothPermissions[0]);
                boolean bluetoothPermission2 = cordova.hasPermission(bluetoothPermissions[1]);

                if (cameraPermission && accesscoarselocationPermission && bluetoothPermission1 && bluetoothPermission2) {
                    JSONObject obj = args.getJSONObject(0);

                    this.initialize(callbackContext, obj);
                    return true;
                } else if (accesscoarselocationPermission) {
                    cordova.requestPermission(this, 0, CAMERA);
                } else if (cameraPermission) {
                    cordova.requestPermission(this, 0, ACCESS_COARSE_LOCATION);
                } else {
                    List<String> missingPermissions = new ArrayList<>();
                    if (!cameraPermission) missingPermissions.add(CAMERA);
                    if (!accesscoarselocationPermission) missingPermissions.add(ACCESS_COARSE_LOCATION);
                    if (!bluetoothPermission1) missingPermissions.add(bluetoothPermissions[0]);
                    if (!bluetoothPermission2) missingPermissions.add(bluetoothPermissions[1]);
                    cordova.requestPermissions(this, 0, missingPermissions.toArray(new String[0]));
                }
                break;

            case "startTransaction": {
                JSONObject obj = args.getJSONObject(0);
                this.startTransaction(callbackContext, obj);
                return true;
            }

            case "requestTransactionCancellation": {
                JSONObject obj = args.getJSONObject(0);
                this.requestTransactionCancellation(callbackContext, obj);
                return true;
            }

            case "onRequestJWTCallback":
                String jwt = args.getString(0);
                this.onRequestJWTCallback(jwt);
                return true;
        }
        return false;
    }

    private void onRequestJWTCallback(String jwt) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (jwtCallback != null) {
                    jwtCallback.invoke(jwt);
                }
                jwtCallback = null;
            }
        });
    }

    private void initialize(CallbackContext callbackContext, JSONObject obj) throws JSONException {
        Long cardTapTimeoutIn =  obj.has("cardTapTimeout") ? Long.parseLong(obj.getString("cardTapTimeout")) : null;
        String applicationNameIn =  obj.has("applicationName") ? obj.getString("applicationName") : null;
        String applicationVersionIn =  obj.has("applicationVersion") ? obj.getString("applicationVersion") : null;

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                HaloInitializationParameters hip = new HaloInitializationParameters(
                        hcb,
                        cardTapTimeoutIn,
                        applicationNameIn,
                        applicationVersionIn
                );
                
                HaloSDK.Companion.initialize(hip);
            }
        });
    }

    private void startTransaction(CallbackContext callbackContext, JSONObject obj)  throws JSONException {
        String merchantReference = obj.has("merchantReference") ? obj.getString("merchantReference") : null;
        Double transactionValue = obj.has("transactionValue") ? obj.getDouble("transactionValue") : null;
        String currency = obj.has("currency") ? obj.getString("currency") : "zar";
        TransactionType transactionType = obj.has("transactionType") ? TransactionType.valueOf(obj.getString("transactionType")) : TransactionType.Purchase;
        CardType cardType = obj.has("cardType") ? CardType.valueOf(obj.getString("cardType")) : null;

        callbackContextId = callbacks.getCallbackId();

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                HaloSDK.Companion.startTransaction(BigDecimal.valueOf(transactionValue), merchantReference, currency, null, null, transactionType, cardType);
            }
        });
    }

    private void requestTransactionCancellation(CallbackContext callbackContext, JSONObject obj)  throws JSONException {
        boolean onPause = obj.has("onPause") && obj.getBoolean("onPause");

        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                HaloSDK.Companion.requestTransactionCancellation(onPause);
            }
        });
    }

    public void setJWTCallback(Function1<? super String, Unit> function) {
        this.jwtCallback = function;
    }

    private String[] getBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            return new String[] {
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            };
        } else {
            return new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            };
        }
    }
}
