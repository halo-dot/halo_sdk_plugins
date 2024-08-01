import 'halo_currency_value.dart';
import 'halo_ui_message_id.dart';

class HaloUIMessage {
  HaloUiMessageId msgId;
  int holdTimeMS;
  List<String>? languagePreference;
  HaloCurrencyValue? offlineBalance;
  HaloCurrencyValue? transactionAmount;

  HaloUIMessage(
    this.msgId,
    this.holdTimeMS,
    this.languagePreference,
    this.offlineBalance,
    this.transactionAmount,
  );
}
