enum HaloErrorCode {
  ok,

  declined,

  declinedOffline,

  singleTapAndPin,

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

  invalidAlgorithm,

  siteIdentifierInvalid,

  siteIdentifierMissing,

  errorWithExternalRequest,

  integratorNotApproved,

  dataError,

  userInvalid,

  unknownDeviceInstallationId,

  requestEncryptionUnsupportedCurve,

  blockedDevice,

  databaseIntegrityFailed,

  databaseError,

  decryptRequestError,

  unknownError,

  cryptoError,

  dukputTransactionCounterOverflow,

  invalidPaymentProvider,

  declinedByPaymentProvider,
  
  failedToSubmitToAllPaymentProviders,

  invalidPinData,

  refundTooLate,

  refundAlreadyProcessed,

  missingPfsServerKey,

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

  useOfNfcStateBeforeConnect,

  failedToImportServerSigningCert,

  failedToImportServerEncryptionKey,

  notImplemented,

  missingSensors,

  microphoneWasUnmuted,

  nullAttestationHandle,

  cryptographyError,

  systemNotInitialised,

  cameraPermissionNotGranted,

  accessibilityServiceBlocksPin,

  developerOptionsEnabled,

  missingAttestationNonce,

  configFetchError,

  missingEntropy,

  invalidTransactionReference,

  missingSecureCardReaderImplementation,

  failedToGetRandomBytes,

  missingDataKek,

  pinError,

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
