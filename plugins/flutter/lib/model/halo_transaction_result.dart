import 'halo_error_code.dart';
import 'halo_transaction_receipt.dart';
import 'halo_transaction_result_type.dart';

class HaloTransactionResult {
  HaloTransactionResultType resultType;

  String? merchantTransactionReference;

  String? haloTransactionReference;

  String? paymentProviderReference;

  HaloErrorCode errorCode;

  List<String> errorDetails;

  HaloTransactionReceipt? receipt;

  Map<String, String>? customTags;

  HaloTransactionResult(
      this.resultType,
      this.merchantTransactionReference,
      this.haloTransactionReference,
      this.paymentProviderReference,
      this.errorCode,
      this.errorDetails,
      this.receipt,
      this.customTags);
}
