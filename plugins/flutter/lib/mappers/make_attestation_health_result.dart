import '../model/halo_attestation_health_result.dart';
import 'make_attestation_health_result_type.dart';
import 'make_halo_error_code.dart';

HaloAttestationHealthResult makeAttestationHealthResult(
    Map<dynamic, dynamic> data) {
  return HaloAttestationHealthResult(
    makeAttestationHealthResultType(data['resultType']),
    makeHaloErrorCode(data['errorCode']),
  );
}
