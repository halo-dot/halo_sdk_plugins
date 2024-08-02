import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'consts/event_types.dart';
import 'consts/method_args.dart';
import 'consts/method_types.dart';
import 'mappers/make_attestation_health_result.dart';
import 'mappers/make_halo_error_code.dart';
import 'mappers/make_halo_initialization_result.dart';
import 'mappers/make_halo_ui_message.dart';
import 'mappers/make_transaction_result.dart';
import 'model/i_halo_callbacks.dart';

class Sdkflutterplugin {
  static const MethodChannel _channel = MethodChannel('sdkflutterplugin');
  static IHaloCallbacks? _haloClientCallbacks;

  static final eventStream =
      EventChannel(EventTypes.haloSdkEventChannel).receiveBroadcastStream();

  static void setCallbackListener(IHaloCallbacks haloCallbacks) {
    _haloClientCallbacks = haloCallbacks;
    eventStream.listen((event) {
      debugPrint("Event received in haloSdkEventChannel: $event");

      switch (event["eventType"]) {
        case (EventTypes.attestation):
          haloCallbacks
              .onAttestationError(makeAttestationHealthResult(event["data"]));
          break;
        case (EventTypes.transaction):
          haloCallbacks
              .onHaloTransactionResult(makeTransactionResult(event["data"]));
          break;
        case (EventTypes.ui):
          haloCallbacks.onHaloUIMessage(makeHaloUiMessage(event["data"]));
          break;
        case (EventTypes.initialization):
          haloCallbacks.onInitializationResult(
              makeHaloInitializationResult(event["data"]));
          break;
        case (EventTypes.onJwtRequest):
          haloCallbacks.onRequestJWT(whenJwtProvided);
          break;
        case (EventTypes.security):
          haloCallbacks.onSecurityError(makeHaloErrorCode(event["data"]));
          break;
        default:
      }
    });
  }

  static Future<void> initializeHaloSDK(
    IHaloCallbacks haloCallbacks,
    String applicationPackageName,
    String applicationVersion,
  ) async {
    _haloClientCallbacks = haloCallbacks;
    setCallbackListener(haloCallbacks);
    Map<dynamic, String> args = {
      MethodArgs.applicationPackageName: applicationPackageName,
      MethodArgs.applicationVersion: applicationVersion
    };
    await _channel.invokeMethod(MethodTypes.initializeHaloSDK, args);
  }

  static Future<void> startTransaction(double transactionAmount,
      String merchantTransactionReference, String transactionCurrency) async {
    Map<String, Object> args = {
      MethodArgs.transactionAmount: transactionAmount,
      MethodArgs.merchantTransactionReference: merchantTransactionReference,
      MethodArgs.transactionCurrency: transactionCurrency
    };
    await _channel.invokeMethod(MethodTypes.startTransaction, args);
  }

  static Future<void> cancelTransaction() async {
    await _channel.invokeMethod(MethodTypes.cancelTransaction);
  }

  static void whenJwtProvided(String jwt) {
    _channel.invokeMethod(MethodTypes.jwtCallback, jwt);
  }
}
