import '../model/halo_ui_message.dart';
import 'make_currency_value.dart';
import 'make_message_id.dart';

HaloUIMessage makeHaloUiMessage(Map<dynamic?, dynamic> data) {
  return HaloUIMessage(
    makeMessageId(data["msgID"]),
    data["holdTimeMS"] as int,
    (data["languagePreference"] as List<dynamic>?)
        ?.map((it) => it as String)
        .toList(),
    makeCurrencyValue(data["offlineBalance"]),
    makeCurrencyValue(data["transactionAmount"]),
  );
}
