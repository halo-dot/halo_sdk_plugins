import './make_currency.dart';
import './make_halo_error_code.dart';
import '../model/halo_initialization_result.dart';
import 'make_halo_initialization_result_type.dart';
import 'make_halo_warnings.dart';

HaloInitializationResult makeHaloInitializationResult(
    Map<dynamic, dynamic> data) {
  return HaloInitializationResult(
      makeHaloInitializationResultType(data['resultType']),
      makeCurrency(data["terminalCurrency"]),
      (data["terminalLanguageCodes"] as List<dynamic>?)
          ?.map((it) => it as String)
          .toList(),
      data["terminalCountryCode"] as String?,
      makeHaloErrorCode(data["errorCode"]),
      makeHaloWarnings(data["warnings"]));
}
