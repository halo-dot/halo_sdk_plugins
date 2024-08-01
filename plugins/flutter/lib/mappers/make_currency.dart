import 'package:sdkflutterplugin/model/currency.dart';

Currency makeCurrency(Map<dynamic, dynamic> data) {
  return Currency(
    data["currencyCode"] as String,
    data["defaultFractionDigits"] as int,
    data["numericCode"] as int,
    data["displayName"] as String,
  );
}
