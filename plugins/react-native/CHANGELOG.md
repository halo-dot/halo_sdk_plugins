# Changelog

All notable changes to `halo-sdk-react-native` will be documented here.

---

## [1.0.5] - 2026-04-22

### Changed
- Updated Halo SDK to 4.0.18

---

## [1.0.4] - 2026-03-04

### Fixed
- Removed `tools:replace` and deprecated `package` attribute from the plugin's `AndroidManifest.xml`. The `tools:replace` directive belongs in the host app's manifest, not the library's, having it in the library manifest caused `processDebugManifest` to fail with "no new value specified".

### Documentation
- Added `tools:replace="android:label,android:allowBackup"` to the `AndroidManifest.xml` example in Getting Started — required because the Halo SDK and its bundled Visa library declare these attributes in their own manifests
- Added `packagingOptions` block to the `build.gradle` setup steps to prevent a duplicate OSGI metadata error from the SDK's transitive dependencies
- Added FAQ entries for manifest merger attribute conflicts, Gradle stale cache (`./gradlew clean`), and TypeScript `moduleResolution`/`customConditions` conflicts
- Fixed Full Example to drop unnecessary default `React` import (not needed with the automatic JSX transform in RN 0.73+)
- Fixed step numbering gap in Getting Started (was 4 → 6, now 1–6 sequential)
- Added Native Module Setup to the Table of Contents

---

## [1.0.3] - 2026-03-04

### Fixed
- Bumped version to trigger npm re-publish after manifest fix was applied (superseded by 1.0.4)

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
