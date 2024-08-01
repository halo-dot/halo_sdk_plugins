package za.co.synthesis.halo.sdkflutterplugin

import android.os.Looper
import android.util.Log
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import za.co.synthesis.halo.haloCommonInterface.HaloErrorCode
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResult
import za.co.synthesis.halo.sdk.model.HaloAttestationHealthResult
import za.co.synthesis.halo.sdk.model.HaloInitializationResult
import za.co.synthesis.halo.sdk.model.HaloUIMessage
import za.co.synthesis.halo.sdk.model.IHaloCallbacks

enum class EventTypes (val eventType: String) {
    ATTESTATION("attestation"),
    TRANSACTION("transaction"),
    UI("ui"),
    INITIALIZATION("initialization"),
    ON_JWT_REQUEST("onJwtRequest"),
    SECURITY("security")
}

class HaloCallbacks(
    messenger: BinaryMessenger
) : IHaloCallbacks() {
    private val TAG = "HaloCallbacks"

    private var eventChannel: EventChannel = EventChannel(messenger, "haloSdkEventChannel")
    private var eventSink: EventChannel.EventSink? = null
    private var _callback: ((String) -> Unit)? = null
    private val handler: android.os.Handler by lazy { android.os.Handler(Looper.getMainLooper()) }
    init {
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                Log.d(TAG, "haloSdkEventChannel onListen...")
                eventSink = events
            }

            override fun onCancel(arguments: Any?) {
                Log.d(TAG, "haloSdkEventChannel onCancel...")
                eventSink = null
            }
        })

    }

    //region IHaloCallbacks
    override fun onAttestationError(details: HaloAttestationHealthResult) {
        Log.d(TAG, "onAttestationError: $details")
        val map = hashMapOf<String, Any>(Pair("eventType", EventTypes.ATTESTATION.eventType), Pair("data", makeMap(details)))
        handler.post {
            eventSink?.success(map)
        }
    }

    override fun onHaloTransactionResult(result: HaloTransactionResult) {
        Log.d(TAG, "onHaloTransactionResult: $result")
        val map = hashMapOf<String, Any>(Pair("eventType", EventTypes.TRANSACTION.eventType), Pair("data", makeMap(result)))
        handler.post {
            eventSink?.success(map)
        }
    }

    override fun onHaloUIMessage(message: HaloUIMessage) {
        Log.d(TAG, "onHaloUIMessage: $message")
        val map = hashMapOf<Any, Any>(Pair("eventType", EventTypes.UI.eventType), Pair("data", makeMap(message)))
        handler.post{
            eventSink?.success(map)
        }
    }

    override fun onInitializationResult(result: HaloInitializationResult) {
        Log.d(TAG, "onInitializationResult: ${result.resultType} ${result.errorCode} ${result.terminalCountryCode} ${result.terminalLanguageCodes} ${result.terminalCurrency}")
        result.warnings.forEach { Log.d(TAG, "onInitializationResult: warning: ${it.details}")}

        val map = hashMapOf<String, Any>(Pair("eventType", EventTypes.INITIALIZATION.eventType), Pair("data", makeMap(result)))
        handler.post {
            eventSink?.success(map)
        }
    }

    override fun onRequestJWT(callback: (String) -> Unit) {
        Log.d(TAG, "onRequestJWT")
        _callback = callback
        handler.post {
            eventSink?.success(mapOf(Pair("eventType",EventTypes.ON_JWT_REQUEST.eventType )))
        }
    }

    override fun onSecurityError(errorCode: HaloErrorCode) {
        Log.d(TAG, "onSecurityError: $errorCode")
        val map = hashMapOf<String, Any>(Pair("eventType", EventTypes.SECURITY.eventType), Pair("data", errorCode.name))

        handler.post {
            eventSink?.success(map)
        }
    }
    //endregion *******************

    fun jwtCallback(jwt: String) {
        _callback?.invoke(jwt)
    }
}
