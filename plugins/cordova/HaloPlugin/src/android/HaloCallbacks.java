package cordovapluginhalo;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import za.co.synthesis.halo.haloCommonInterface.HaloErrorCode;
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResult;
import za.co.synthesis.halo.sdk.model.HaloAttestationHealthResult;
import za.co.synthesis.halo.sdk.model.HaloInitializationResult;
import za.co.synthesis.halo.sdk.model.HaloUIMessage;
import za.co.synthesis.halo.sdk.model.IHaloCallbacks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HaloCallbacks extends IHaloCallbacks {
    static String TAG = "HaloCallbacks";
    HaloPlugin hp;

    public HaloCallbacks(HaloPlugin haloPlugin){
        hp = haloPlugin;
    }

    private final Gson gson = new GsonBuilder()
            .create();

    @Override
    public void onInitializationResult(@NonNull HaloInitializationResult haloInitializationResult) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "onInitializationResult");
            String jsonString = gson.toJson(haloInitializationResult);
            jsonObject.put("result", jsonString);

            PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
            callbackResult.setKeepCallback(true);
            hp.callbacks.sendPluginResult(callbackResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHaloUIMessage(@NonNull HaloUIMessage haloUIMessage) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "onHaloUIMessage");
            String jsonString = gson.toJson(haloUIMessage);
            jsonObject.put("result", jsonString);

            PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
            callbackResult.setKeepCallback(true);

            for (CallbackContext callbackContext: hp.callbackContextList) {
                if (hp.callbackContextId != null && callbackContext.getCallbackId().equalsIgnoreCase(hp.callbackContextId)) {
                    callbackContext.sendPluginResult(callbackResult);
                    return;
                }
            }

            hp.callbacks.sendPluginResult(callbackResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHaloTransactionResult(@NonNull HaloTransactionResult haloTransactionResult) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "onHaloTransactionResult");

            JSONObject jsonObjectHaloTransactionResult = new JSONObject();
            jsonObjectHaloTransactionResult.put("resultType", haloTransactionResult.getResultType());
            jsonObjectHaloTransactionResult.put("merchantTransactionReference", haloTransactionResult.getMerchantTransactionReference());
            jsonObjectHaloTransactionResult.put("haloTransactionReference", haloTransactionResult.getHaloTransactionReference());
            jsonObjectHaloTransactionResult.put("paymentProviderReference", haloTransactionResult.getPaymentProviderReference());
            jsonObjectHaloTransactionResult.put("errorCode", haloTransactionResult.getErrorCode());
            jsonObjectHaloTransactionResult.put("errorDetails", haloTransactionResult.getErrorDetails());

            JSONObject jsonObjectHaloTransactionReceipt = new JSONObject();
            jsonObjectHaloTransactionReceipt.put("signature", haloTransactionResult.getReceipt().getSignature());
            jsonObjectHaloTransactionReceipt.put("transactionDate", haloTransactionResult.getReceipt().getTransactionDate());
            jsonObjectHaloTransactionReceipt.put("transactionTime", haloTransactionResult.getReceipt().getTransactionTime());
            jsonObjectHaloTransactionReceipt.put("aid", haloTransactionResult.getReceipt().getAid());
            jsonObjectHaloTransactionReceipt.put("applicationLabel", haloTransactionResult.getReceipt().getApplicationLabel());
            jsonObjectHaloTransactionReceipt.put("applicationPreferredName", haloTransactionResult.getReceipt().getApplicationPreferredName());
            jsonObjectHaloTransactionReceipt.put("tvr", haloTransactionResult.getReceipt().getTvr());
            jsonObjectHaloTransactionReceipt.put("cvr", haloTransactionResult.getReceipt().getCvr());
            jsonObjectHaloTransactionReceipt.put("cryptogramType", haloTransactionResult.getReceipt().getCryptogramType());
            jsonObjectHaloTransactionReceipt.put("cryptogram", haloTransactionResult.getReceipt().getCryptogram());
            jsonObjectHaloTransactionReceipt.put("maskedPAN", haloTransactionResult.getReceipt().getMaskedPAN());
            jsonObjectHaloTransactionReceipt.put("authorizationCode", haloTransactionResult.getReceipt().getAuthorizationCode());
            jsonObjectHaloTransactionReceipt.put("ISOResponseCode", haloTransactionResult.getReceipt().getISOResponseCode());
            jsonObjectHaloTransactionReceipt.put("association", haloTransactionResult.getReceipt().getAssociation());
            jsonObjectHaloTransactionReceipt.put("expiryDate", haloTransactionResult.getReceipt().getExpiryDate());
            jsonObjectHaloTransactionReceipt.put("mid", haloTransactionResult.getReceipt().getMid());
            jsonObjectHaloTransactionReceipt.put("merchantName", haloTransactionResult.getReceipt().getMerchantName());
            jsonObjectHaloTransactionReceipt.put("tid", haloTransactionResult.getReceipt().getTid());
            jsonObjectHaloTransactionReceipt.put("stan", haloTransactionResult.getReceipt().getStan());
            jsonObjectHaloTransactionReceipt.put("panEntry", haloTransactionResult.getReceipt().getPanEntry());
            jsonObjectHaloTransactionReceipt.put("cardType", haloTransactionResult.getReceipt().getCardType());
            jsonObjectHaloTransactionReceipt.put("panSequenceNumber", haloTransactionResult.getReceipt().getPanSequenceNumber());
            jsonObjectHaloTransactionReceipt.put("effectiveDate", haloTransactionResult.getReceipt().getEffectiveDate());
            jsonObjectHaloTransactionReceipt.put("disposition", haloTransactionResult.getReceipt().getDisposition());
            jsonObjectHaloTransactionReceipt.put("currencyCode", haloTransactionResult.getReceipt().getCurrencyCode());
            jsonObjectHaloTransactionReceipt.put("amountAuthorised", haloTransactionResult.getReceipt().getAmountAuthorised());
            jsonObjectHaloTransactionReceipt.put("amountOther", haloTransactionResult.getReceipt().getAmountOther());
            jsonObjectHaloTransactionResult.put("receipt", jsonObjectHaloTransactionReceipt);

            jsonObjectHaloTransactionResult.put("customTags", haloTransactionResult.getCustomTags());
            jsonObject.put("result", jsonObjectHaloTransactionResult);

            PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
            callbackResult.setKeepCallback(true);

            for (CallbackContext callbackContext: hp.callbackContextList) {
                if (hp.callbackContextId != null && callbackContext.getCallbackId().equalsIgnoreCase(hp.callbackContextId)) {
                    callbackContext.sendPluginResult(callbackResult);
                    return;
                }
            }
            hp.callbacks.sendPluginResult(callbackResult);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
          CallbackContext callbackContextRemove = null;
          for (CallbackContext callbackContext: hp.callbackContextList){
            if (hp.callbackContextId != null &&
                callbackContext.getCallbackId().equalsIgnoreCase(hp.callbackContextId)){
                callbackContextRemove = callbackContext;
            }
          }

          if (callbackContextRemove != null){
            hp.callbackContextList.remove(callbackContextRemove);
          }

          hp.callbackContextId = null;
        }
    }

    @Override
    public void onRequestJWT(@NonNull Function1<? super String, Unit> function1) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", "onRequestJWT");

            PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
            callbackResult.setKeepCallback(true);
            hp.callbacks.sendPluginResult(callbackResult);
            hp.setJWTCallback(function1);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttestationError(@NonNull HaloAttestationHealthResult haloAttestationHealthResult) {
       try {
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("action", "onAttestationError");
           String jsonString = gson.toJson(haloAttestationHealthResult);
           jsonObject.put("result", jsonString);

           PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
           callbackResult.setKeepCallback(true);
           hp.callbacks.sendPluginResult(callbackResult);
       } catch (JSONException e) {
           e.printStackTrace();
       }
    }

    @Override
    public void onSecurityError(@NonNull HaloErrorCode haloErrorCode) {
       try {
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("action", "onSecurityError");
           String jsonString = gson.toJson(haloErrorCode);
           jsonObject.put("result", jsonString);

           PluginResult callbackResult = new PluginResult(PluginResult.Status.OK, jsonObject);
           callbackResult.setKeepCallback(true);
           hp.callbacks.sendPluginResult(callbackResult);
       } catch (JSONException e) {
           e.printStackTrace();
       }
    }
}