import 'currency.dart';
import 'halo_error_code.dart';
import 'halo_initialization_result_type.dart';
import 'halo_warning.dart';

class HaloInitializationResult {
  HaloInitializationResultType? resultType;
  Currency? terminalCurrency;
  List<String>? terminalLanguageCodes;
  String? terminalCountryCode;
  HaloErrorCode? errorCode;
  List<HaloWarning>? warnings;

  HaloInitializationResult(
    this.resultType,
    this.terminalCurrency,
    this.terminalLanguageCodes,
    this.terminalCountryCode,
    this.errorCode,
    this.warnings,
  );
}
