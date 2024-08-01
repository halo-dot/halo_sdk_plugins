import 'package:sdkflutterplugin/model/halo_transaction_result_type.dart';

HaloTransactionResultType makeHaloTransactionResultType(String value) {
  switch (value) {
    case ('Approved'):
      return HaloTransactionResultType.approved;
    case ('Declined'):
      return HaloTransactionResultType.declined;
    case ('CardTapTimeOutExpired'):
      return HaloTransactionResultType.cardTapTimeOutExpired;
    case ('NetworkError'):
      return HaloTransactionResultType.networkError;
    case ('ProcessingError'):
      return HaloTransactionResultType.processingError;
    case ('Cancelled'):
      return HaloTransactionResultType.cancelled;
    case ('TryAnotherCard'):
      return HaloTransactionResultType.tryAnotherCard;
    case ('NFCDisabledError'):
      return HaloTransactionResultType.nfcDisabledError;
    case ('NotAuthenticated'):
      return HaloTransactionResultType.notAuthenticated;
    case ('Indeterminate'):
      return HaloTransactionResultType.indeterminate;
    case ('DuplicateMerchantTransactionReferenceSupplied'):
      return HaloTransactionResultType
          .duplicateMerchantTransactionReferenceSupplied;
    case ('HealthError'):
      return HaloTransactionResultType.healthError;
    case ('InvalidJWT'):
      return HaloTransactionResultType.invalidJWT;
    case ('BluetoothDeviceUnavailable'):
      return HaloTransactionResultType.bluetoothDeviceUnavailable;
    default:
      throw Exception('Unknown HaloTransactionResultType value: $value');
  }
}
