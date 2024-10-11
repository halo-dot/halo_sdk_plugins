import './make_halo_error_code.dart';
import '../model/halo_start_transaction_result.dart';
import 'make_halo_start_transaction_result_type.dart';

HaloStartTransactionResult makeHaloStartTransactionResult(
    Map<dynamic, dynamic> data) {
  return HaloStartTransactionResult(
      makeHaloStartTransactionResultType(data["resultType"] as String),
      makeHaloErrorCode(data["errorCode"] as String?));
}
