import '../model/halo_currency_value.dart';
import 'make_currency.dart';

HaloCurrencyValue? makeCurrencyValue(Map<dynamic, dynamic>? data) {
  if (data == null || data["currency"] == null) {
    return null;
  }
  return HaloCurrencyValue(
    data["amount"] as double,
    makeCurrency(data["currency"])!,
  );
}
