import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'mappers/make_attestation_health_result.dart';
import 'mappers/make_halo_error_code.dart';
import 'mappers/make_halo_initialization_result.dart';
import 'mappers/make_halo_ui_message.dart';
import 'mappers/make_transaction_result.dart';
import 'model/i_halo_callbacks.dart';

class EventTypes {
  static const attestation = "attestation";
  static const transaction = "transaction";
  static const ui = "ui";
  static const initialization = "initialization";
  static const onJwtRequest = "onJwtRequest";
  static const security = "security";
}

class Sdkflutterplugin {
  static const MethodChannel _channel = MethodChannel('sdkflutterplugin');
  static IHaloCallbacks? _haloClientCallbacks;

  static final eventStream =
      EventChannel('haloSdkEventChannel').receiveBroadcastStream();

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
      "applicationPackageName": applicationPackageName,
      "applicationVersion": applicationVersion
    };
    await _channel.invokeMethod('initializeHaloSDK', args);
  }

  static Future<void> startTransaction(double transactionAmount,
      String merchantTransactionReference, String transactionCurrency) async {
    Map<String, Object> args = {
      "transactionAmount": transactionAmount,
      "merchantTransactionReference": merchantTransactionReference,
      "transactionCurrency": transactionCurrency
    };
    await _channel.invokeMethod('startTransaction', args);
  }

  static Future<void> cancelTransaction() async {
    await _channel.invokeMethod('cancelTransaction');
  }

  static void whenJwtProvided(String jwt) {
    _channel.invokeMethod('jwtCallback', jwt);
  }
}
