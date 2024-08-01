/**
 * Ideally your server will issue the JWT and this config will not be needed,
 * For testing purposes you create the JWT on the device using this config
 */
class Config {
  static const String privateKeyPem = """-----BEGIN PRIVATE KEY-----
-----END PRIVATE KEY-----"""; // <-- add private key here (Don't commit your private key)
  static const String publicKey = """-----BEGIN PUBLIC KEY-----
-----END PUBLIC KEY-----"""; // <-- add public key here
  static const String issuer = ""; // <-- add issuer here
  static const String username = ""; // <-- add username here
  static const String merchantId = ""; // <-- add merchant ID here
  static const String host = "kernelserver.za.dev.haloplus.io";
}
