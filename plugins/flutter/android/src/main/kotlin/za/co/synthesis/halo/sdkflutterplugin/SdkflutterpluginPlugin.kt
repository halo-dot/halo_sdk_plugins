package za.co.synthesis.halo.sdkflutterplugin

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** SdkflutterpluginPlugin */
class SdkflutterpluginPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will handle the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var methodChannel : MethodChannel

  private lateinit var haloSdkImplementation: HaloSdkImplementation

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "sdkflutterplugin")
    methodChannel.setMethodCallHandler(this)

    haloSdkImplementation = HaloSdkImplementation(flutterPluginBinding.binaryMessenger)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    try {
      when (call.method) {
        "initializeHaloSDK" -> haloSdkImplementation.initializeHaloSDK(result, call.arguments<HashMap<String, Any>>())
        "startTransaction" -> haloSdkImplementation.startTransaction(result, call.arguments<HashMap<String, Any>>())
        "jwtCallback" -> haloSdkImplementation.jwtCallback(result, call.arguments<String>())
        "cancelTransaction" -> haloSdkImplementation.cancelTransaction(result)
        else -> result.notImplemented()
      }
    } catch (e: Exception) {
      result.error("sdkflutterplugin error", e.message, e)
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    methodChannel.setMethodCallHandler(null)
  }
}
