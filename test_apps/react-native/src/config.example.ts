/**
 * EXAMPLE CONFIGURATION — rename this file to config.ts and fill in your values.
 *
 * config.ts is gitignored to avoid committing real credentials.
 * This file serves as a template showing every available option.
 */
export const Config = {
  // paste a pre-generated JWT here for quick testing, or fill in the private key fields to generate JWTs at runtime.
  tempJwt: 'eyJ...',

  privateKeyPem: '',
  issuer: 'za.co.your.company.mpos',
  username: '+27771234567',
  merchantId: 'your-merchant-id',
  host: 'kernelserver.za.dev.haloplus.io',

  // Halo SDK initialisation parameters
  applicationPackageName: 'com.your.package.name',
  applicationVersion: '1.0.0',
  onStartTransactionTimeOut: 300000, // in seconds
  enableSchemeAnimations: true, // show or hide card scheme animations on transaction approval
} as const;
