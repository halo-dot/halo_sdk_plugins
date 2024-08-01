package za.co.synthesis.halo.sdkflutterplugin

import android.util.Log
import io.flutter.plugin.common.BinaryMessenger
import za.co.synthesis.halo.sdk.HaloSDK
import za.co.synthesis.halo.sdk.model.HaloInitializationParameters

class HaloSdkImplementation(messanger: BinaryMessenger) {
  private val TAG = "HaloSdkImplim"
  private lateinit var haloCallbacks: HaloCallbacks
  private val ON_START_TRANSACTION_TIME_OUT = 300000L

  init {
    haloCallbacks = HaloCallbacks(messanger)
  }

  fun initializeHaloSDK(args: HashMap<String, String>): Any? {
    Log.d(TAG, "initializeHaloSDK: $args")

    HaloSDK.initialize(
      HaloInitializationParameters(
        haloCallbacks,
        ON_START_TRANSACTION_TIME_OUT,
        args[Const.APPLICATION_PACKAGE_NAME] as String,
        args[Const.APPLICATION_VERSION] as String
      )
    )
    return null
  }

  fun cancelTransaction(): Any? {
    Log.d(TAG, "cancelTransaction")
    HaloSDK.requestTransactionCancellation()
    return null
  }

  fun startTransaction(args: HashMap<String, Any>): Any? {
    Log.d(TAG, "startTransaction: $args")

    val extraFields = getRest<String>(args, listOf(
      Const.TRANSACTION_AMOUNT,
      Const.MERCHANT_TRANSACTION_REFERENCE,
      Const.TRANSACTION_CURRENCY
    ))

    HaloSDK.startTransaction(
      (args[Const.TRANSACTION_AMOUNT] as Double).toBigDecimal(),
      args[Const.MERCHANT_TRANSACTION_REFERENCE] as String,
      args[Const.TRANSACTION_CURRENCY] as String,
      extraFields
    )
    return null
  }

  fun jwtCallback(args: Any): Any? {
    Log.d(TAG, "jwtCallback: $args")
    haloCallbacks.jwtCallback(args as String)
    return null
  }
}
