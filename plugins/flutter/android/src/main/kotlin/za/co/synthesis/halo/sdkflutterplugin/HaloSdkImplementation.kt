package za.co.synthesis.halo.sdkflutterplugin

import android.util.Log
import io.flutter.plugin.common.BinaryMessenger
import za.co.synthesis.halo.sdk.HaloSDK
import za.co.synthesis.halo.sdk.model.HaloInitializationParameters
import io.flutter.plugin.common.MethodChannel.Result
import za.co.synthesis.halo.haloCommonInterface.HaloErrorCode
import za.co.synthesis.halo.haloCommonInterface.HaloException
import kotlin.reflect.typeOf

class HaloSdkImplementation(messanger: BinaryMessenger) {
  private val TAG = "HaloSdkImplim"
  private lateinit var haloCallbacks: HaloCallbacks
  private val ON_START_TRANSACTION_TIME_OUT = 300000L

  init {
    haloCallbacks = HaloCallbacks(messanger)
  }

  fun initializeHaloSDK(result: Result, args: HashMap<String, Any>) {
    Log.d(TAG, "initializeHaloSDK: $args")

    try {
      HaloSDK.initialize(
        HaloInitializationParameters(
          haloCallbacks,
          (args[Const.ON_START_TRANSACTION_TIME_OUT] as Int?)?.toLong() ?: ON_START_TRANSACTION_TIME_OUT,
          args[Const.APPLICATION_PACKAGE_NAME] as String,
          args[Const.APPLICATION_VERSION] as String
        )
      )

      result.success(null)
    } catch (e: Exception) {
      if (e is HaloException) {
        result.error("sdkflutterplugin error", e.message, e.errorCode)
      } else {
        result.error("sdkflutterplugin error", e.message, null)
      }
    }

  }

  fun cancelTransaction(result: Result) {
    Log.d(TAG, "cancelTransaction")
    HaloSDK.requestTransactionCancellation()
    result.success(null)
  }

  fun startTransaction(result: Result, args: HashMap<String, Any>) {
    Log.d(TAG, "startTransaction: $args")

    val extraFields = getRest<String>(args, listOf(
      Const.TRANSACTION_AMOUNT,
      Const.MERCHANT_TRANSACTION_REFERENCE,
      Const.TRANSACTION_CURRENCY
    ))

    try {
      val startTransactionResult = HaloSDK.startTransaction(
        (args[Const.TRANSACTION_AMOUNT] as Double).toBigDecimal(),
        args[Const.MERCHANT_TRANSACTION_REFERENCE] as String,
        args[Const.TRANSACTION_CURRENCY] as String,
        extraFields
      )

      result.success(makeMap(startTransactionResult))
    } catch (e: Exception) {
      if (e is HaloException) {
        result.error("sdkflutterplugin error", e.message, e.errorCode)
      } else {
        result.error("sdkflutterplugin error", e.message, null)
      }
    }
  }

  fun jwtCallback(result: Result, args: String) {
    Log.d(TAG, "jwtCallback: $args")
    haloCallbacks.jwtCallback(args)
    result.success(null)
  }
}
