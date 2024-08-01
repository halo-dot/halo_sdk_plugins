import '../model/halo_warning.dart';
import 'make_halo_error_code.dart';

List<HaloWarning> makeHaloWarnings(List<dynamic>? data) {
  if (data == null) {
    return [];
  }
  return data
      .map((it) => HaloWarning(
            makeHaloErrorCode(it["errorCode"] as String),
            (it["details"] as List<dynamic>?)
                    ?.map((it) => it as String)
                    .toList() ??
                [],
          ))
      .toList();
}
