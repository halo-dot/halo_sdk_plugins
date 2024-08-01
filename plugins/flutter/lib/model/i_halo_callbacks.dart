import 'halo_attestation_health_result.dart';
import 'halo_error_code.dart';
import 'halo_initialization_result.dart';
import 'halo_transaction_result.dart';
import 'halo_ui_message.dart';

abstract class IHaloCallbacks {
  void onAttestationError(HaloAttestationHealthResult details);

  void onHaloTransactionResult(HaloTransactionResult result);

  void onHaloUIMessage(HaloUIMessage message);

  void onInitializationResult(HaloInitializationResult result);

  void onRequestJWT(void Function(String jwt) callback);

  void onSecurityError(HaloErrorCode errorCode);
}
