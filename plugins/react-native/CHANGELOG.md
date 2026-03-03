# Changelog

All notable changes to `halo-sdk-react-native` will be documented here.

---

## [1.0.2] - 2026-03-03

### Fixed
- Changed Halo SDK dependency declaration from `compileOnly` to `api` in `build.gradle` so it is exposed transitively to host apps. This prevents a `NoClassDefFoundError: HaloSDK` crash on launch,  host apps no longer need to add the Halo SDK as a direct dependency.

### Documentation
- Updated README to remove the now-unnecessary manual Halo SDK runtime dependency step
- Fixed incorrect `resultType` check in code examples (`'success'`-> `'Initialized'`)
- Added Result Types reference table for `HaloInitializationResult` and `HaloTransactionResult`
- Added FAQ entries for common setup issues (Metro bundler `SyntaxError`, `JWTExpired` on init)
- Corrected the JWT section to reference official Halo documentation
- Fixed Full Example to use `requestHaloPermissions()` from `src/permissions.ts`

---

## [1.0.1] - Initial release

- Initial React Native plugin wrapping the Halo Dot Android SDK
- Supports `initialize`, `startTransaction`, `cardRefundTransaction`, and `cancelTransaction`
- Event-driven callbacks via `NativeEventEmitter`: initialization, transaction result, UI messages, JWT requests, attestation errors, security errors, and camera events
- Scheme animations (Visa / Mastercard / Amex) on approved transactions via `AnimationActivity`
