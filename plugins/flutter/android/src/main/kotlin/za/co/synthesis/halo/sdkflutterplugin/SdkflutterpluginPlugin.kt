package za.co.synthesis.halo.sdkflutterplugin

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** SdkflutterpluginPlugin */
class SdkflutterpluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var methodChannel : MethodChannel

  private lateinit var haloSdkImplementation: HaloSdkImplementation

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "sdkflutterplugin")
    methodChannel.setMethodCallHandler(this)

    haloSdkImplementation = HaloSdkImplementation(flutterPluginBinding.binaryMessenger)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    try {
      when (call.method) {
        "initializeHaloSDK" -> result.success(haloSdkImplementation.initializeHaloSDK(getHashMapFromArguments(call.arguments())))
        "startTransaction" -> result.success(haloSdkImplementation.startTransaction(getHashMapFromArguments(call.arguments())))
        "jwtCallback" -> result.success(haloSdkImplementation.jwtCallback(call.arguments()))
        "cancelTransaction" -> result.success(haloSdkImplementation.cancelTransaction())
        else -> result.notImplemented()
      }
    } catch (e: Exception) {
      result.error("sdkflutterplugin error", e.message, e)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
  }
}
