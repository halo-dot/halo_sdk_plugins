import '../model/halo_initialization_result_type.dart';

HaloInitializationResultType makeHaloInitializationResultType(String value) {
  switch (value) {
    case "Initialized":
      return HaloInitializationResultType.initialized;
    case "InitializedWithoutConfigs":
      return HaloInitializationResultType.initializedWithoutConfigs;
    case "AuthenticationError":
      return HaloInitializationResultType.authenticationError;
    case "NetworkError":
      return HaloInitializationResultType.networkError;
    case "UnsupportedOperatingSystemVersion":
      return HaloInitializationResultType.unsupportedOperatingSystemVersion;
    case "UnsupportedDevice":
      return HaloInitializationResultType.unsupportedDevice;
    case "RootedDevice":
      return HaloInitializationResultType.rootedDevice;
    case "InstrumentedDevice":
      return HaloInitializationResultType.instrumentedDevice;
    case "DebuggedDevice":
      return HaloInitializationResultType.debuggedDevice;
    case "GeneralError":
      return HaloInitializationResultType.generalError;
    case "RemoteAttestationFailure":
      return HaloInitializationResultType.remoteAttestationFailure;
    case "NFCDisabledError":
      return HaloInitializationResultType.nfcDisabledError;
    case "AttestationFailure":
      return HaloInitializationResultType.attestationFailure;
    case "TEEAttestationFailure":
      return HaloInitializationResultType.teeAttestationFailure;
    case "AttestationSystemNotInitialised":
      return HaloInitializationResultType.attestationSystemNotInitialised;
    case "HttpClientError":
      return HaloInitializationResultType.httpClientError;
    case "ConfigFetchError":
      return HaloInitializationResultType.configFetchError;
    case "UnableToConfigureTerminal":
      return HaloInitializationResultType.unableToConfigureTerminal;
    case "NoTerminalContainer":
      return HaloInitializationResultType.noTerminalContainer;
    case "NoAppContext":
      return HaloInitializationResultType.noAppContext;
    case "CameraPermissionNotGranted":
      return HaloInitializationResultType.cameraPermissionNotGranted;
    default:
      throw Exception("Unknown HaloInitializationResultType value: $value");
  }
}
