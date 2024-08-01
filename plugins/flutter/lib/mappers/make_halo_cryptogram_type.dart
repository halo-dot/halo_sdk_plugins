import 'package:sdkflutterplugin/model/halo_cryptogram_type.dart';

HaloCryptogramType? makeHaloCryptogramType(String cryptogramType) {
  switch (cryptogramType) {
    case "AAC":
      return HaloCryptogramType.aac;
    case "TC":
      return HaloCryptogramType.tc;
    case "ARQC":
      return HaloCryptogramType.arqc;
    default:
      return null;
  }
}
