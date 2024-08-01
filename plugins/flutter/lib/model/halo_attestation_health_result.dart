import 'attestation_health_result_type.dart';
import 'halo_error_code.dart';

class HaloAttestationHealthResult {
  AttestationHealthResultType resultType;
  HaloErrorCode errorCode;

  HaloAttestationHealthResult(this.resultType, this.errorCode);
}
