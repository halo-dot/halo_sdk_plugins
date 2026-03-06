package za.co.synthesis.halo.sdkreactnativeplugin

import android.util.Log
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import za.co.synthesis.halo.haloCommonInterface.TransactionType

class HaloSdkModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext),
    LifecycleEventListener {

    private val TAG = "HaloSdkModule"
    private val haloSdkImplementation: HaloSdkImplementation = HaloSdkImplementation(reactContext)

    init {
        reactContext.addLifecycleEventListener(this)
    }

    override fun getName(): String = "HaloSdk"

    @ReactMethod
    fun initializeHaloSDK(args: ReadableMap, promise: Promise) {
        @Suppress("UNCHECKED_CAST")
        haloSdkImplementation.initializeHaloSDK(promise, args.toHashMap() as HashMap<String, Any>)
    }

    @ReactMethod
    fun startTransaction(args: ReadableMap, promise: Promise) {
        @Suppress("UNCHECKED_CAST")
        haloSdkImplementation.startTransaction(promise, args.toHashMap() as HashMap<String, Any>)
    }

    @ReactMethod
    fun cardRefundTransaction(args: ReadableMap, promise: Promise) {
        @Suppress("UNCHECKED_CAST")
        haloSdkImplementation.startTransaction(promise, args.toHashMap() as HashMap<String, Any>, TransactionType.Refund)
    }

    @ReactMethod
    fun jwtCallback(jwt: String, promise: Promise) {
        haloSdkImplementation.jwtCallback(promise, jwt)
    }

    @ReactMethod
    fun cancelTransaction(promise: Promise) {
        haloSdkImplementation.cancelTransaction(promise)
    }

    // Required for RN NativeEventEmitter
    @ReactMethod
    fun addListener(eventName: String) {}

    @ReactMethod
    fun removeListeners(count: Int) {}

    // Keep UIContext activity reference up to date
    override fun onHostResume() {
        Log.d(TAG, "onHostResume")
        UIContext.updateActivity(reactApplicationContext.currentActivity)
    }

    override fun onHostPause() {
        Log.d(TAG, "onHostPause")
    }

    override fun onHostDestroy() {
        Log.d(TAG, "onHostDestroy")
        UIContext.updateActivity(null)
    }
}
