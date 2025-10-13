package za.co.synthesis.halo.printlib

import MobiPosPrinter
import android.content.Context

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

enum class PrinterProvider {
  MOBIPOS,
}

class PrintlibPlugin: FlutterPlugin, MethodCallHandler{
  private lateinit var channel : MethodChannel
  private var context: Context? = null
  private var printer: Printer? = null

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    context = flutterPluginBinding.applicationContext
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "printlib")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
      "initializePrinter" -> handleInitializePrinter(call, result)
      "printText" -> handlePrintText(call, result)
      "printHTML" -> handlePrintHTML(call, result)
      else -> result.notImplemented()
    }
  }

  private fun handleInitializePrinter(call: MethodCall, result: Result) {
    try {
      val providerName = call.argument<String>("provider")
        ?: return result.error(
          "MISSING_ARGUMENT",
          "Argument 'provider' is required",
          null
        )

      initializePrinter(providerName)
      result.success(true)
    } catch (e: IllegalArgumentException) {
      result.error("INVALID_PROVIDER", e.message, null)
    } catch (e: Exception) {
      result.error("INITIALIZATION_ERROR", e.message, null)
    }
  }

  private fun handlePrintText(call: MethodCall, result: Result) {
    try {
      if(printer == null){
        return result.error("NOT_INITIALIZED", "Printer not initialized. Call 'initializePrinter' method to initialize.", null)
      }
      val text = call.argument<String>("text")
        ?: return result.error("MISSING_ARGUMENT", "Argument 'text' is required", null)

      printer?.printText(text)
      result.success(true)
    } catch (e: Exception) {
      result.error("PRINT_ERROR", e.message, null)
    }
  }

  private fun handlePrintHTML(call: MethodCall, result: Result) {
    try {
      if(printer == null){
        return result.error("NOT_INITIALIZED", "Printer not initialized. Call 'initializePrinter' method to initialize.", null)
      }

      val html = call.argument<String>("html")
        ?: return result.error("MISSING_ARGUMENT", "Argument 'html' is required", null)

      printer!!.printHTML(html)
      result.success(true)
    } catch (e: NotImplementedError) {
      result.error("NOT_IMPLEMENTED", "HTML printing not yet supported", null)
    } catch (e: Exception) {
      result.error("PRINT_ERROR", e.message, null)
    }
  }

  private fun initializePrinter(providerName: String) {
    val provider = try {
      PrinterProvider.valueOf(providerName.uppercase())
    } catch (e: IllegalArgumentException) {
      throw IllegalArgumentException(
        "Invalid provider '$providerName'. Available providers: ${PrinterProvider.values().joinToString()}"
      )
    }


    printer = when (provider) {
      PrinterProvider.MOBIPOS -> MobiPosPrinter()
    }

    printer?.initialize(context)
  }

  private fun cleanupPrinter() {
      printer?.close()
      printer = null
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    cleanupPrinter()
    channel.setMethodCallHandler(null)
    context = null
  }
}
