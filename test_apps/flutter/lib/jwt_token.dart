import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:sdkflutterplugin_example/config.dart';

// if you provide a JWT token, it will be used instead of generating a new one
const String tempJwt = "";

class JwtToken {
  static String getJwt() {
    if (tempJwt.isNotEmpty) {
      return tempJwt;
    }

    // be sure you have the correct values in the Config class

    final jwt = JWT(
      {'aud_fingerprints': '', 'ksk_pin': '', 'usr': Config.username},
      audience: Audience([Config.host]),
      issuer: Config.issuer,
      subject: Config.merchantId,
    );

    final key = RSAPrivateKey(Config.privateKeyPem);
    final token = jwt.sign(key, algorithm: JWTAlgorithm.RS512);

    return token;
  }
}
