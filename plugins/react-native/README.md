# halo-sdk-react-native

React Native plugin for the Halo SDK, enabling Android NFC card-present payment transactions.

## Requirements

- React Native 0.73+
- Android SDK 29+ (minSdkVersion 29)
- NFC-capable Android device

## Installation

### 1. Copy the plugin

Place the plugin source alongside your app (or publish it to a registry). In your app's `package.json`, link it:

```json
"dependencies": {
  "halo-sdk-react-native": "file:../path/to/plugins/react-native"
}
```

### 2. Add the Halo AAR libraries

Copy the following AARs into your Android project's `android/libs/` folder:

- `VisaSensoryBranding.aar`
- `sonic-sdk-release-1.5.0.aar`

### 3. Configure AWS credentials for the Halo Maven repository

The plugin fetches the Halo SDK from an S3-backed Maven repo. Provide credentials in `android/local.properties`:

```properties
aws.accesskey=YOUR_ACCESS_KEY
aws.secretkey=YOUR_SECRET_KEY
aws.token=YOUR_SESSION_TOKEN   # optional
```

Or set environment variables `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, and `AWS_SESSION_TOKEN`.

### 4. Register the native module

In your `MainApplication`, add `HaloSdkPackage` to `getPackages()`:

```kotlin
override fun getPackages(): List<ReactPackage> = listOf(
    MainReactPackage(),
    HaloSdkPackage(),
)
```

### 5. Extend HaloReactActivity

In `MainActivity`, extend `HaloReactActivity` instead of `ReactActivity`:

```kotlin
import za.co.synthesis.halo.sdkreactnativeplugin.HaloReactActivity

class MainActivity : HaloReactActivity() {
    override fun getMainComponentName(): String = "YourAppName"
    override fun createReactActivityDelegate() =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
```

This ensures Halo SDK lifecycle management and NFC foreground dispatch work correctly.

## Usage

```typescript
import {
  HaloSdk,
  type IHaloCallbacks,
  type HaloInitializationResult,
  type HaloTransactionResult,
  type HaloUIMessage,
  type HaloAttestationHealthResult,
} from 'halo-sdk-react-native';

const callbacks: IHaloCallbacks = {
  onInitializationResult(result: HaloInitializationResult) {
    console.log('Initialized:', result.resultType);
  },
  onHaloTransactionResult(result: HaloTransactionResult) {
    console.log('Transaction:', result.resultType, result.errorCode);
  },
  onHaloUIMessage(message: HaloUIMessage) {
    console.log('UI Message:', message.msgID);
  },
  onAttestationError(details: HaloAttestationHealthResult) {
    console.error('Attestation error:', details.errorCode);
  },
  onRequestJWT(jwtCallback: (jwt: string) => void) {
    const jwt = getJwtFromYourServer();
    jwtCallback(jwt);
  },
  onSecurityError(errorCode: string) {
    console.error('Security error:', errorCode);
  },
  onCameraControlLost() {
    console.warn('Camera control lost');
  },
};

// Initialize once (e.g. on app start)
await HaloSdk.initialize(
  callbacks,
  'com.your.package.name',
  '1.0.0',
  300000, // transaction timeout in ms (optional, default 300000)
  true,   // enable scheme animations (optional)
);

// Start a purchase
const result = await HaloSdk.startTransaction(10.50, 'REF-001', 'ZAR');

// Start a card refund
const refund = await HaloSdk.cardRefundTransaction(10.50, 'REF-001', 'ZAR');

// Cancel current transaction
await HaloSdk.cancelTransaction();
```

## API

### `HaloSdk.initialize(callbacks, packageName, version, timeout?, animations?)`

Initialises the SDK. Must be called before any transaction methods.

| Parameter | Type | Description |
|-----------|------|-------------|
| `callbacks` | `IHaloCallbacks` | Event handler object |
| `applicationPackageName` | `string` | Your app's package name |
| `applicationVersion` | `string` | Your app's version string |
| `onStartTransactionTimeOut` | `number?` | Tap timeout in ms (default 300000) |
| `enableSchemeAnimations` | `boolean?` | Show Visa/Mastercard/Amex animations on approval |

### `HaloSdk.startTransaction(amount, reference, currency)`

Starts a purchase transaction. Resolves with a `HaloStartTransactionResult` once the card tap is accepted or rejected.

### `HaloSdk.cardRefundTransaction(amount, reference, currency)`

Starts a card-present refund transaction.

### `HaloSdk.cancelTransaction()`

Requests cancellation of the current in-progress transaction.

## Callbacks (`IHaloCallbacks`)

| Callback                   | Description                                                           |
|----------------------------|-----------------------------------------------------------------------|
| `onInitializationResult`   | SDK initialisation complete or failed                                 |
| `onHaloTransactionResult`  | Final transaction outcome                                             |
| `onHaloUIMessage`          | Prompt to display to the cardholder (e.g. "Present card")             |
| `onRequestJWT`             | SDK needs a fresh JWT; call the provided `jwtCallback` with the token |
| `onAttestationError`       | Device attestation failed                                             |
| `onSecurityError`          | Security check failed                                                 |
| `onCameraControlLost`      | SDK released camera control                                           |

## Building the plugin

```bash
npm install
npm run build
```
