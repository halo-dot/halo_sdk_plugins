package za.co.synthesis.halo.sdkreactnativeplugin

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import za.co.synthesis.halo.haloCommonInterface.HaloException
import za.co.synthesis.halo.haloCommonInterface.TransactionType
import za.co.synthesis.halo.sdk.HaloSDK
import za.co.synthesis.halo.sdk.model.HaloInitializationParameters

class HaloSdkImplementation(reactContext: ReactApplicationContext) {
    private val TAG = "HaloSdkImplementation"
    private val ON_START_TRANSACTION_TIME_OUT = 300000L
    private val haloCallbacks: HaloCallbacks = HaloCallbacks(reactContext)

    fun initializeHaloSDK(promise: Promise, args: HashMap<String, Any>) {
        Log.d(TAG, "initializeHaloSDK: $args")
        try {
            HaloSDK.initialize(
                HaloInitializationParameters(
                    haloCallbacks,
                    (args[Const.ON_START_TRANSACTION_TIME_OUT] as? Number)?.toLong()
                        ?: ON_START_TRANSACTION_TIME_OUT,
                    args[Const.APPLICATION_PACKAGE_NAME] as String,
                    args[Const.APPLICATION_VERSION] as String
                )
            )
            UIContext.enableSchemeAnimations(
                (args[Const.ENABLE_SCHEME_ANIMATIONS] as Boolean?) ?: false
            )
            promise.resolve(null)
        } catch (e: Exception) {
            if (e is HaloException) {
                promise.reject("sdkreactnativeplugin error", e.message, e)
            } else {
                promise.reject("sdkreactnativeplugin error", e.message, e)
            }
        }
    }

    fun startTransaction(
        promise: Promise,
        args: HashMap<String, Any>,
        transactionType: TransactionType = TransactionType.Purchase
    ) {
        Log.d(TAG, "startTransaction: $args")
        try {
            val extraFields = getRest<String>(
                args,
                listOf(
                    Const.TRANSACTION_AMOUNT,
                    Const.MERCHANT_TRANSACTION_REFERENCE,
                    Const.TRANSACTION_CURRENCY
                )
            )
            val result = HaloSDK.startTransaction(
                (args[Const.TRANSACTION_AMOUNT] as Double).toBigDecimal(),
                args[Const.MERCHANT_TRANSACTION_REFERENCE] as String,
                args[Const.TRANSACTION_CURRENCY] as String,
                extraFields,
                null,
                transactionType
            )
            promise.resolve(makeWritableMap(result))
        } catch (e: Exception) {
            promise.reject("sdkreactnativeplugin error", e.message, e)
        }
    }

    fun cancelTransaction(promise: Promise) {
        Log.d(TAG, "cancelTransaction")
        HaloSDK.requestTransactionCancellation()
        promise.resolve(null)
    }

    fun jwtCallback(promise: Promise, jwt: String) {
        Log.d(TAG, "jwtCallback")
        haloCallbacks.jwtCallback(jwt)
        promise.resolve(null)
    }
}
