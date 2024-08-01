import 'package:sdkflutterplugin/model/halo_transaction_result.dart';

import 'make_halo_error_code.dart';
import 'make_halo_transaction_receipt.dart';
import 'make_halo_transaction_result_type.dart';
import 'make_map.dart';

HaloTransactionResult makeTransactionResult(Map<dynamic, dynamic> data) {
  return HaloTransactionResult(
      makeHaloTransactionResultType(data['resultType']),
      data['merchantTransactionReference'] as String?,
      data['haloTransactionReference'] as String?,
      data['paymentProviderReference'] as String?,
      makeHaloErrorCode(data['errorCode']),
      (data['errorDetails'] as List<dynamic>?)
              ?.map((it) => it as String)
              ?.toList() ??
          [],
      makeHaloTransactionReceipt(data['receipt']),
      makeMap<String, String>((data['customTags'] as Map<dynamic, dynamic>?)));
}
