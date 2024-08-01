import 'dart:typed_data';

import 'halo_cryptogram_type.dart';

class HaloTransactionReceipt {
  Uint8List signature;
  String transactionDate;
  String transactionTime;
  String? aid;
  String? applicationLabel;
  String? applicationPreferredName;
  String? tvr;
  String? cvr;
  HaloCryptogramType? cryptogramType;
  String? cryptogram;
  String maskedPAN;
  String authorizationCode;
  String ISOResponseCode;
  String association;
  String? expiryDate;
  String? mid;
  String? merchantName;
  String? tid;
  String? stan;
  String? panEntry;
  String? cardType;
  String? panSequenceNumber;
  String? effectiveDate;
  String? disposition;
  String? currencyCode;
  String? amountAuthorised;
  String? amountOther;

  HaloTransactionReceipt(
    this.signature,
    this.transactionDate,
    this.transactionTime,
    this.aid,
    this.applicationLabel,
    this.applicationPreferredName,
    this.tvr,
    this.cvr,
    this.cryptogramType,
    this.cryptogram,
    this.maskedPAN,
    this.authorizationCode,
    this.ISOResponseCode,
    this.association,
    this.expiryDate,
    this.mid,
    this.merchantName,
    this.tid,
    this.stan,
    this.panEntry,
    this.cardType,
    this.panSequenceNumber,
    this.effectiveDate,
    this.disposition,
    this.currencyCode,
    this.amountAuthorised,
    this.amountOther,
  );
}
