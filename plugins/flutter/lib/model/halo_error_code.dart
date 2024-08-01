enum HaloErrorCode {
  ok,

  declined,

  unauthorised,

  forbidden,

  invalidApiKey,

  invalidJSON,

  invalidRequest,

  blockedUser,

  requestSignatureInvalid,

  requestSignatureMissing,

  deviceKeyMissing,

  deviceKeyInvalid,

  replayedMessage,

  requestSigningKeyMissing,

  requestSigningKeyInvalid,

  requestEncryptionKeyMissing,

  requestEncryptionKeyInvalid,

  deviceNotAttested,

  deviceFailedAttestation,

  jwtInvalid,

  jwtExpired,

  idAlreadyExists,

  phoneNumberNotVerified,

  emailNotVerified,

  dataNotFound,

  transactionDoesNotBelongToMerchant,

  duplicateMerchantReference,

  receiptSignatureInvalid,

  databaseIntegrityFailed,

  databaseError,

  decryptRequestError,

  unknownError,

  nfcDisabled,

  invalidSystemState,

  invalidCurrency,

  invalidAndroidVersion,

  errorParsingJWT,

  networkError,

  responseSignatureMissing,

  responseSignatureInvalid,

  serverRequestedConfigClear,

  errorDeserialising,

  safetyNetAttestationError,

  googlePlayUnavailable,

  timeDriftTooGreat,

  attestationInProgress,

  rootedDevice,

  instrumentedDevice,

  debuggedDevice,

  cancelledTransaction,

  cardTapTimeoutExpired,

  transactionError,

  teeAttestationError,

  errorValidatingPinKey,

  integrityCheckFailed,

  systemNotInitialised,

  cameraPermissionNotGranted,

  accessibilityServiceBlocksPin,

  developerOptionsBlockPin,

  missingAttestationNonce,

  configFetchError,

  missingEntropy,

  invalidTransactionReference,

  missingSecureCardReaderImplementation,

  bluetoothConnectPermissionsNotGranted,

  bluetoothScanPermissionNotGranted,

  bluetoothUnavailable,

  bluetoothScanInProgress,

  bluetoothPairFailed,

  secureCardReaderCommandFailed,

  bluetoothDeviceUnavailable,

  secureCardReaderTimeout,

  secureCardReaderConfigInvalid,

  bluetoothNotEnabled,

  secureCardReaderPinError,

  secureCardReaderPluginNotAvailableForDevice,

  secureCardReaderTransactionInProgress,

  bluetoothFineLocationPermissionsNotGranted,

  bluetoothPairTimeout
}
