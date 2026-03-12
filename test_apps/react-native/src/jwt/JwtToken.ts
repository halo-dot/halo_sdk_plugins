import { Config } from '../config';

/**
 * Returns a JWT for the Halo SDK's onRequestJWT callback.
 *
 * For quick testing: set Config.tempJwt to a pre-generated token.
 *
 * For dynamic generation: integrate a library such as react-native-pure-jwt
 * and implement RS512 signing using Config.privateKeyPem, Config.issuer,
 * Config.username, Config.merchantId, and Config.host.
 */
export function getJwt(): string {
  if (Config.tempJwt) {
    return Config.tempJwt;
  }

  // TODO: implement dynamic JWT generation thingie if needed, e.g.:
  // const jwt = await RNPureJwt.sign(payload, Config.privateKeyPem, { alg: 'RS512' });
  // return jwt;

  throw new Error(
    'No JWT available.\n' +
      'Set Config.tempJwt in src/config.ts with a pre-generated token, ' +
      'or implement RSA signing using react-native-pure-jwt.',
  );
}
