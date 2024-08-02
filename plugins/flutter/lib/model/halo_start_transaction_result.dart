import 'halo_error_code.dart';
import 'halo_start_transaction_result_type.dart';

class HaloStartTransactionResult {
  HaloStartTransactionResultType resultType;
  HaloErrorCode errorCode;

  HaloStartTransactionResult(this.resultType, this.errorCode);
}
