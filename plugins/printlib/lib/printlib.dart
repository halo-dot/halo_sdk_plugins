import 'package:flutter/services.dart';

/// Supported printer providers for the printlib plugin.
///
/// This enum defines the available printer providers that can be used
/// with the printlib plugin for printing functionality.
enum Providers {
  // ignore: constant_identifier_names
  MOBIPOS
}

/// A Flutter plugin for integrating with POS devices that have printing capabilities.
///
/// The Printlib class provides a unified interface for printing text and HTML content
/// to various POS printer devices. It uses platform channels to communicate with
/// native Android implementations for actual printer communication.
///
/// Example usage:
/// ```dart
/// final printlib = Printlib();
///
/// // Initialize the printer
/// await printlib.initializePrinter(Providers.MOBIPOS);
///
/// // Print some text
/// await printlib.printText("Hello, World!");
///
/// // Print HTML content
/// await printlib.printHTML("<h1>Receipt</h1><p>Total: \$25.00</p>");
/// ```
class Printlib {
  static const _channel = MethodChannel("printlib");

  /// Initializes the printer with the specified provider.
  ///
  /// This method sets up the printer connection and prepares it for printing
  /// operations. It must be called before attempting to print any content.
  ///
  /// The initialization process may include:
  /// - Establishing connection to the printer device
  /// - Configuring printer settings
  /// - Verifying printer availability and status
  ///
  /// Parameters:
  /// - [provider]: The printer provider to use for printing operations.
  ///   Currently supports [Providers.MOBIPOS].
  ///
  /// Throws:
  /// - [PlatformException] if the printer initialization fails
  /// - [MissingPluginException] if the platform implementation is not available
  ///
  /// Example:
  /// ```dart
  /// try {
  ///   await printlib.initializePrinter(Providers.MOBIPOS);
  ///   print('Printer initialized successfully');
  /// } catch (e) {
  ///   print('Failed to initialize printer: $e');
  /// }
  /// ```
  Future<void> initializePrinter(Providers provider) async {
    await _channel
        .invokeMethod("initializePrinter", {"provider": provider.name});
  }

  /// Prints plain text content to the connected printer.
  ///
  /// This method sends plain text to the printer for output. The text will be
  /// printed using the default formatting settings of the printer device.
  ///
  /// The printer must be initialized using [initializePrinter] before calling
  /// this method.
  ///
  /// Parameters:
  /// - [text]: The plain text content to print. Cannot be null.
  ///   Special characters and line breaks (\n) are supported.
  ///
  /// Throws:
  /// - [PlatformException] if the printing operation fails
  /// - [MissingPluginException] if the platform implementation is not available
  /// - [ArgumentError] if the text parameter is null
  ///
  /// Example:
  /// ```dart
  /// try {
  ///   await printlib.printText('Hello, World!\nThis is a new line.');
  ///   print('Text printed successfully');
  /// } catch (e) {
  ///   print('Failed to print text: $e');
  /// }
  /// ```
  Future<void> printText(String text) async {
    await _channel.invokeMethod("printText", {"text": text});
  }

  /// Prints HTML-formatted content to the connected printer.
  ///
  /// This method sends HTML content to the printer, which will be parsed and
  /// rendered according to the printer's HTML capabilities. The level of HTML
  /// support may vary depending on the printer provider and device.
  ///
  /// The printer must be initialized using [initializePrinter] before calling
  /// this method.
  ///
  ///
  /// Note: CSS styling and advanced HTML features are not be supported
  /// on all printer devices.
  ///
  /// Parameters:
  /// - [html]: The HTML content to print. Must be valid HTML markup.
  ///   Cannot be null.
  ///
  /// Throws:
  /// - [PlatformException] if the printing operation fails
  /// - [MissingPluginException] if the platform implementation is not available
  /// - [ArgumentError] if the html parameter is null
  /// - [FormatException] if the HTML content is malformed
  ///
  /// Example:
  /// ```dart
  /// try {
  ///   await printlib.printHTML('''
  ///     <h1>Receipt</h1>
  ///     <p><b>Date:</b> 2025-10-06</p>
  ///     <p><b>Total:</b> \$25.00</p>
  ///   ''');
  ///   print('HTML printed successfully');
  /// } catch (e) {
  ///   print('Failed to print HTML: $e');
  /// }
  /// ```
  Future<void> printHTML(String html) async {
    await _channel.invokeMethod("printHTML", {"html": html});
  }
}
