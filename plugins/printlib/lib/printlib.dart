import 'dart:ffi';

import 'package:flutter/services.dart';

enum Providers {
  // ignore: constant_identifier_names
  MOBIPOS
}

class Printlib {
  static const _channel = MethodChannel("printlib");

  Future<bool?> initializePrinter(Providers provider) async {
    bool res = await _channel
        .invokeMethod("initializePrinter", {"provider": provider.name});
    return res;
  }

  Future<void> printText(
      {required String text,
      Int? textSize,
      bool? isBold,
      bool? isUnderlined}) async {
    await _channel.invokeMethod("printText", {
      "text": text,
      "textSize": textSize,
      "isBold": isBold,
      "isUnderlined": isUnderlined
    });
  }

  Future<void> printHTML(String html) async {
    await _channel.invokeMethod("printHTML", {"html": html});
  }
}
