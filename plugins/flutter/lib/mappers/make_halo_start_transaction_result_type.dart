import '../model/halo_start_transaction_result_type.dart';

HaloStartTransactionResultType makeHaloStartTransactionResultType(
    String value) {
  switch (value) {
    case ("NotInitialized"):
      return HaloStartTransactionResultType.notInitialized;
    case ("Started"):
      return HaloStartTransactionResultType.started;
    case ("GeneralError"):
      return HaloStartTransactionResultType.generalError;
    case ("NFCDisabledError"):
      return HaloStartTransactionResultType.nfcDisabledError;
    case ("RootedDevice"):
      return HaloStartTransactionResultType.rootedDevice;
    case ("InstrumentedDevice"):
      return HaloStartTransactionResultType.instrumentedDevice;
    case ("DebuggedDevice"):
      return HaloStartTransactionResultType.debuggedDevice;
    case ("InvalidJWT"):
      return HaloStartTransactionResultType.invalidJWT;
    case ("UnableToActivateTerminal"):
      return HaloStartTransactionResultType.unableToActivateTerminal;
    case ("InvalidCurrency"):
      return HaloStartTransactionResultType.invalidCurrency;
    case ("NoAppContext"):
      return HaloStartTransactionResultType.noAppContext;
    default:
      throw Exception("Unknown HaloStartTransactionResultType value: $value");
  }
}
