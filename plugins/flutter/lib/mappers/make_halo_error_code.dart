import '../model/halo_error_code.dart';

HaloErrorCode makeHaloErrorCode(String? value) {
  switch (value) {
    case 'OK':
      return HaloErrorCode.ok;
    case 'Declined':
      return HaloErrorCode.declined;
    case 'Unauthorised':
      return HaloErrorCode.unauthorised;
    case 'Forbidden':
      return HaloErrorCode.forbidden;
    case 'InvalidApiKey':
      return HaloErrorCode.invalidApiKey;
    case 'InvalidJSON':
      return HaloErrorCode.invalidJSON;
    case 'InvalidRequest':
      return HaloErrorCode.invalidRequest;
    case 'BlockedUser':
      return HaloErrorCode.blockedUser;
    case 'RequestSignatureInvalid':
      return HaloErrorCode.requestSignatureInvalid;
    case 'RequestSignatureMissing':
      return HaloErrorCode.requestSignatureMissing;
    case 'DeviceKeyMissing':
      return HaloErrorCode.deviceKeyMissing;
    case 'DeviceKeyInvalid':
      return HaloErrorCode.deviceKeyInvalid;
    case 'ReplayedMessage':
      return HaloErrorCode.replayedMessage;
    case 'RequestSigningKeyMissing':
      return HaloErrorCode.requestEncryptionKeyMissing;
    case 'RequestSigningKeyInvalid':
      return HaloErrorCode.requestEncryptionKeyInvalid;
    case 'RequestEncryptionKeyMissing':
      return HaloErrorCode.requestEncryptionKeyMissing;
    case 'RequestEncryptionKeyInvalid':
      return HaloErrorCode.requestEncryptionKeyInvalid;
    case 'DeviceNotAttested':
      return HaloErrorCode.deviceNotAttested;
    case 'DeviceFailedAttestation':
      return HaloErrorCode.deviceFailedAttestation;
    case 'JWTInvalid':
      return HaloErrorCode.jwtInvalid;
    case 'JWTExpired':
      return HaloErrorCode.jwtExpired;
    case 'IdAlreadyExists':
      return HaloErrorCode.idAlreadyExists;
    case 'PhoneNumberNotVerified':
      return HaloErrorCode.phoneNumberNotVerified;
    case 'EmailNotVerified':
      return HaloErrorCode.emailNotVerified;
    case 'DataNotFound':
      return HaloErrorCode.dataNotFound;
    case 'TransactionDoesNotBelongToMerchant':
      return HaloErrorCode.transactionDoesNotBelongToMerchant;
    case 'DuplicateMerchantReference':
      return HaloErrorCode.duplicateMerchantReference;
    case 'ReceiptSignatureInvalid':
      return HaloErrorCode.receiptSignatureInvalid;
    case 'DatabaseIntegrityFailed':
      return HaloErrorCode.databaseIntegrityFailed;
    case 'DatabaseError':
      return HaloErrorCode.databaseError;
    case 'DecryptRequestError':
      return HaloErrorCode.decryptRequestError;
    case 'UnknownError':
      return HaloErrorCode.unknownError;
    case 'NFCDisabled':
      return HaloErrorCode.nfcDisabled;
    case 'InvalidSystemState':
      return HaloErrorCode.invalidSystemState;
    case 'InvalidCurrency':
      return HaloErrorCode.invalidCurrency;
    case 'InvalidAndroidVersion':
      return HaloErrorCode.invalidAndroidVersion;
    case 'ErrorParsingJWT':
      return HaloErrorCode.errorParsingJWT;
    case 'NetworkError':
      return HaloErrorCode.networkError;
    case 'ResponseSignatureMissing':
      return HaloErrorCode.responseSignatureMissing;
    case 'ResponseSignatureInvalid':
      return HaloErrorCode.responseSignatureInvalid;
    case 'ServerRequestedConfigClear':
      return HaloErrorCode.serverRequestedConfigClear;
    case 'ErrorDeserialising':
      return HaloErrorCode.errorDeserialising;
    case 'SafetyNetAttestationError':
      return HaloErrorCode.safetyNetAttestationError;
    case 'GooglePlayUnavailable':
      return HaloErrorCode.googlePlayUnavailable;
    case 'TimeDriftTooGreat':
      return HaloErrorCode.timeDriftTooGreat;
    case 'AttestationInProgress':
      return HaloErrorCode.attestationInProgress;
    case 'RootedDevice':
      return HaloErrorCode.rootedDevice;
    case 'InstrumentedDevice':
      return HaloErrorCode.instrumentedDevice;
    case 'DebuggedDevice':
      return HaloErrorCode.debuggedDevice;
    case 'CancelledTransaction':
      return HaloErrorCode.cancelledTransaction;
    case 'CardTapTimeoutExpired':
      return HaloErrorCode.cardTapTimeoutExpired;
    case 'TransactionError':
      return HaloErrorCode.transactionError;
    case 'TEEAttestationError':
      return HaloErrorCode.teeAttestationError;
    case 'ErrorValidatingPinKey':
      return HaloErrorCode.errorValidatingPinKey;
    case 'IntegrityCheckFailed':
      return HaloErrorCode.integrityCheckFailed;
    case 'SystemNotInitialised':
      return HaloErrorCode.systemNotInitialised;
    case 'CameraPermissionNotGranted':
      return HaloErrorCode.cameraPermissionNotGranted;
    case 'AccessibilityServiceBlocksPin':
      return HaloErrorCode.accessibilityServiceBlocksPin;
    case 'DeveloperOptionsBlockPin':
      return HaloErrorCode.developerOptionsBlockPin;
    case 'MissingAttestationNonce':
      return HaloErrorCode.missingAttestationNonce;
    case 'ConfigFetchError':
      return HaloErrorCode.configFetchError;
    case 'MissingEntropy':
      return HaloErrorCode.missingEntropy;
    case 'InvalidTransactionReference':
      return HaloErrorCode.invalidTransactionReference;
    case 'MissingSecureCardReaderImplementation':
      return HaloErrorCode.missingSecureCardReaderImplementation;
    case 'BluetoothConnectPermissionsNotGranted':
      return HaloErrorCode.bluetoothConnectPermissionsNotGranted;
    case 'BluetoothScanPermissionNotGranted':
      return HaloErrorCode.bluetoothScanPermissionNotGranted;
    case 'BluetoothUnavailable':
      return HaloErrorCode.bluetoothUnavailable;
    case 'BluetoothScanInProgress':
      return HaloErrorCode.bluetoothScanInProgress;
    case 'BluetoothPairFailed':
      return HaloErrorCode.bluetoothPairFailed;
    case 'SecureCardReaderCommandFailed':
      return HaloErrorCode.secureCardReaderCommandFailed;
    case 'BluetoothDeviceUnavailable':
      return HaloErrorCode.bluetoothDeviceUnavailable;
    case 'SecureCardReaderTimeout':
      return HaloErrorCode.secureCardReaderTimeout;
    case 'SecureCardReaderConfigInvalid':
      return HaloErrorCode.secureCardReaderConfigInvalid;
    case 'BluetoothNotEnabled':
      return HaloErrorCode.bluetoothNotEnabled;
    case 'SecureCardReaderPinError':
      return HaloErrorCode.secureCardReaderPinError;
    case 'SecureCardReaderPluginNotAvailableForDevice':
      return HaloErrorCode.secureCardReaderPluginNotAvailableForDevice;
    case 'SecureCardReaderTransactionInProgress':
      return HaloErrorCode.secureCardReaderTransactionInProgress;
    case 'BluetoothFineLocationPermissionsNotGranted':
      return HaloErrorCode.bluetoothFineLocationPermissionsNotGranted;
    case 'BluetoothPairTimeout':
      return HaloErrorCode.bluetoothPairTimeout;
    default:
      throw Exception('Unknown HaloErrorCode value: $value');
  }
}
