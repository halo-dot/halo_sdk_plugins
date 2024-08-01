import '../model/attestation_health_result_type.dart';

AttestationHealthResultType makeAttestationHealthResultType(String value) {
  switch (value) {
    case 'OK':
      return AttestationHealthResultType.ok;
    case 'HTTP_ERROR':
      return AttestationHealthResultType.httpError;
    case 'TIME_ERROR':
      return AttestationHealthResultType.timeError;
    default:
      throw Exception('Unknown AttestationHealthResultType value: $value');
  }
}
