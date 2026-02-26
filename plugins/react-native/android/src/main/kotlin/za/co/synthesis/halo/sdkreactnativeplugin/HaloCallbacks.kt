package za.co.synthesis.halo.sdkreactnativeplugin

import android.content.Intent
import android.os.Looper
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import za.co.synthesis.halo.haloCommonInterface.HaloErrorCode
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResult
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResultType
import za.co.synthesis.halo.sdk.model.HaloAttestationHealthResult
import za.co.synthesis.halo.sdk.model.HaloInitializationResult
import za.co.synthesis.halo.sdk.model.HaloUIMessage
import za.co.synthesis.halo.sdk.model.IHaloCallbacks

const val HALO_SDK_EVENT = "haloSdkEvent"

class HaloCallbacks(
    private val reactContext: ReactApplicationContext
) : IHaloCallbacks() {
    private val TAG = "HaloCallbacks"
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var _jwtCallback: ((String) -> Unit)? = null

    private fun sendEvent(params: WritableMap) {
        handler.post {
            if (reactContext.hasActiveCatalystInstance()) {
                reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit(HALO_SDK_EVENT, params)
            }
        }
    }

    override fun onAttestationError(details: HaloAttestationHealthResult) {
        Log.d(TAG, "onAttestationError: $details")
        val params = Arguments.createMap().apply {
            putString("eventType", "attestation")
            putMap("data", makeWritableMap(details))
        }
        sendEvent(params)
    }

    override fun onHaloTransactionResult(result: HaloTransactionResult) {
        Log.d(TAG, "onHaloTransactionResult: $result")
        val association = "${result.receipt?.association}"
        val maskedPAN = "${result.receipt?.maskedPAN}"
        val transactionApproved = result.resultType == HaloTransactionResultType.Approved

        val params = Arguments.createMap().apply {
            putString("eventType", "transaction")
            putMap("data", makeWritableMap(result))
        }

        handler.post {
            val context = UIContext.getActivity()
            val showSchemeAnimations = UIContext.areSchemeAnimationsEnabled()
            if (context != null && transactionApproved && showSchemeAnimations) {
                val intent = Intent(context, AnimationActivity::class.java)
                intent.putExtra(Const.CARD_ASSOCIATION, association)
                intent.putExtra(Const.MASKED_PAN, maskedPAN)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            if (reactContext.hasActiveCatalystInstance()) {
                reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit(HALO_SDK_EVENT, params)
            }
        }
    }

    override fun onHaloUIMessage(message: HaloUIMessage) {
        Log.d(TAG, "onHaloUIMessage: $message")
        val params = Arguments.createMap().apply {
            putString("eventType", "ui")
            putMap("data", makeWritableMap(message))
        }
        sendEvent(params)
    }

    override fun onInitializationResult(result: HaloInitializationResult) {
        Log.d(TAG, "onInitializationResult resultType: ${result.resultType}")
        Log.d(TAG, "onInitializationResult errorCode: ${result.errorCode}")

        val params = Arguments.createMap().apply {
            putString("eventType", "initialization")
            putMap("data", makeWritableMap(result))
        }
        sendEvent(params)
    }

    override fun onRequestJWT(callback: (String) -> Unit) {
        Log.d(TAG, "onRequestJWT")
        _jwtCallback = callback
        val params = Arguments.createMap().apply {
            putString("eventType", "onJwtRequest")
        }
        sendEvent(params)
    }

    override fun onSecurityError(errorCode: HaloErrorCode) {
        Log.d(TAG, "onSecurityError: $errorCode")
        val params = Arguments.createMap().apply {
            putString("eventType", "security")
            putString("data", errorCode.name)
        }
        sendEvent(params)
    }

    override fun onCameraControlLost() {
        Log.d(TAG, "onCameraControlLost")
        val params = Arguments.createMap().apply {
            putString("eventType", "camera")
            putString("data", "onCameraControlLost")
        }
        sendEvent(params)
    }

    fun jwtCallback(jwt: String) {
        _jwtCallback?.invoke(jwt)
    }
}
