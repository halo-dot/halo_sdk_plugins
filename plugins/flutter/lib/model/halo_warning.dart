import 'halo_error_code.dart';

class HaloWarning {
  HaloErrorCode errorCode;
  List<String> details;

  HaloWarning(this.errorCode, this.details);
}
