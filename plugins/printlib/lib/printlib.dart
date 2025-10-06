import 'package:flutter/services.dart';

enum Providers {
  // ignore: constant_identifier_names
  MOBIPOS
}

class Printlib {
  static const _channel = MethodChannel("printlib");

  Future<void> initializePrinter(Providers provider) async {
    await _channel
        .invokeMethod("initializePrinter", {"provider": provider.name});
  }

  Future<void> printText(String text) async {
    await _channel.invokeMethod("printText", {"text": text});
  }

  Future<void> printHTML(String html) async {
    await _channel.invokeMethod("printHTML", {"html": html});
  }
}
