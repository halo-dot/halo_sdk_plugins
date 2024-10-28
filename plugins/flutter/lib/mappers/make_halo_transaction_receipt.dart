import 'dart:typed_data';
import '../model/halo_transaction_receipt.dart';
import 'make_halo_cryptogram_type.dart';

HaloTransactionReceipt? makeHaloTransactionReceipt(
    Map<dynamic, dynamic>? data) {
  if (data == null) {
    return null;
  }
  return HaloTransactionReceipt(
    data['signature'] as Uint8List,
    data['transactionDate'] as String,
    data['transactionTime'] as String,
    data['aid'] as String?,
    data['applicationLabel'] as String?,
    data['applicationPreferredName'] as String?,
    data['tvr'] as String?,
    data['cvr'] as String?,
    makeHaloCryptogramType(data['cryptogramType']),
    data['cryptogram'] as String?,
    data['maskedPAN'] as String,
    data['authorizationCode'] as String,
    data['ISOResponseCode'] as String,
    data['association'] as String,
    data['expiryDate'] as String?,
    data['mid'] as String?,
    data['merchantName'] as String?,
    data['tid'] as String?,
    data['stan'] as String?,
    data['panEntry'] as String?,
    data['cardType'] as String?,
    data['panSequenceNumber'] as String?,
    data['effectiveDate'] as String?,
    data['disposition'] as String?,
    data['currencyCode'] as String?,
    data['amountAuthorised'] as String?,
    data['amountOther'] as String?,
  );
}
