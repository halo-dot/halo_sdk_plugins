enum HaloInitializationResultType {
  initialized,

  initializedWithoutConfigs,

  authenticationError,

  networkError,

  unsupportedOperatingSystemVersion,

  unsupportedDevice,

  rootedDevice,

  instrumentedDevice,

  debuggedDevice,

  generalError,

  remoteAttestationFailure,

  nfcDisabledError,

  attestationFailure,

  teeAttestationFailure,

  attestationSystemNotInitialised,

  httpClientError,

  configFetchError,

  unableToConfigureTerminal,

  noTerminalContainer,

  noAppContext,

  cameraPermissionNotGranted
}
