# halo-sdk-react-native

A React Native plugin of the [Halo Dot SDK](https://halo-dot-developer-docs.gitbook.io/halo-dot/sdk/1.-getting-started).

The Halo Dot SDK is an Isolating MPoC SDK payment processing software with Attestation & Monitoring Capabilities. It turns an NFC-capable Android phone into a card-present payment terminal, no extra hardware required.

The diagram below shows the SDK boundary and how it interacts with your app, the cardholder's card, and the Halo payment gateway.

![Halo Dot SDK Architecture](https://static.dev.haloplus.io/static/mpos/readme/assets/full_process_MIPS_1200.png)

## Table of Contents

- [Requirements](#requirements)
- [Developer Portal Registration](#developer-portal-registration)
- [Getting Started](#getting-started)
  - [Create a React Native App](#create-a-react-native-app)
  - [Environment Setup](#environment-setup)
  - [Plugin Installation](#plugin-installation)
  - [Native Module Setup](#native-module-setup)
  - [AndroidManifest Permissions](#androidmanifest-permissions)
  - [JWT — What It Is and How to Set It Up](#jwt--what-it-is-and-how-to-set-it-up)
- [Usage](#usage)
  - [Step 1 — Request Permissions](#step-1--request-permissions)
  - [Step 2 — Set Up Callbacks](#step-2--set-up-callbacks)
  - [Step 3 — Initialize the SDK](#step-3--initialize-the-sdk)
  - [Step 4 — Run a Transaction](#step-4--run-a-transaction)
  - [Full Example](#full-example)
- [API Reference](#api-reference)
  - [Result Types](#result-types)
- [Documentation](#documentation)
- [Testing](#testing)
- [FAQ](#faq)

---

## Requirements

| Requirement | Minimum |
|---|---|
| [React Native](https://reactnative.dev/) | 0.73+ |
| [Java](https://www.java.com/en/) | 21 |
| Android `minSdkVersion` | 29 |
| Android `compileSdkVersion` / `targetSdkVersion` | 34+ |
| Device | NFC-capable Android phone |
| IDE | [Android Studio](https://developer.android.com/studio/install) recommended |

You will also need:
- A developer account on the [Halo developer portal](https://go.developerportal.qa.haloplus.io/)
- A signed Non-Disclosure Agreement (NDA), available on the portal
- A JWT — generated via the developer portal or your own backend (see [JWT section](#jwt--what-it-is-and-how-to-set-it-up))

> **Note:** Android is the only supported platform for now.

---

## Developer Portal Registration

You must register on the QA (UAT) environment before going to production.

1. Go to [go.developerportal.qa.haloplus.io](https://go.developerportal.qa.haloplus.io/) and create an account
2. Verify your account via OTP
3. Click **Access the SDK**
   ![access sdk](https://static.dev.haloplus.io/static/mpos/readme/assets/access_sdk.jpg)
4. Download and accept the NDA
5. Submit your RSA **public key** and create an **Issuer name** — these are used to verify the JWTs your app will sign
   ![public key](https://static.dev.haloplus.io/static/mpos/readme/assets/public_key.png)
6. Copy your **Access Key** and **Secret Key** — you will need these to download the SDK from the Halo Maven repo
   ![access key](https://static.dev.haloplus.io/static/mpos/readme/assets/access_key.png)

---

## Getting Started

### Create a React Native App

If you don't already have a React Native project, create one:

```bash
npx react-native init MyHaloApp
cd MyHaloApp
```

### Environment Setup

1. Make sure you have **Java 21** installed. Run `java -version` to check.

2. Open `android/app/build.gradle` and confirm `minSdkVersion` is `29` or higher:

```gradle
android {
    defaultConfig {
        applicationId "com.yourcompany.myapp"
        minSdkVersion 29       // <-- must be 29+
        targetSdkVersion 34
        compileSdkVersion 34
        // ...
    }
}
```

3. See the [FAQ](#faq) if you have trouble with these SDK version settings.

### Plugin Installation

**1.** Install the npm package:

```bash
npm install halo-sdk-react-native
# or
yarn add halo-sdk-react-native
```

**2.** The plugin downloads the Halo SDK binaries from an S3-backed Maven repo. Add your credentials (from the [developer portal](#developer-portal-registration)) to `android/local.properties` (create the file if it doesn't exist):

```properties
aws.accesskey=YOUR_ACCESS_KEY
aws.secretkey=YOUR_SECRET_KEY
```

> **Important:** Never commit `local.properties` to source control. Add it to your `.gitignore`

**3.** Add the following snippet to `android/app/build.gradle` so Gradle can read `local.properties` (it may already be there in newer RN templates):

```gradle
def localProperties = new Properties()
def localPropertiesFile = rootProject.file('local.properties')
if (localPropertiesFile.exists()) {
    localPropertiesFile.withReader('UTF-8') { reader ->
        localProperties.load(reader)
    }
}
```

**4.** Add the following `packagingOptions` block inside the `android { }` closure in `android/app/build.gradle`. This prevents a duplicate-file error caused by OSGI metadata bundled in the SDK's transitive dependencies:

```gradle
android {
    // ... your existing config ...

    packagingOptions {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}
```

### Native Module Setup

**5.** Open `android/app/src/main/kotlin/.../MainActivity.kt` and extend `HaloReactActivity` instead of `ReactActivity`:

```kotlin
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import za.co.synthesis.halo.sdkreactnativeplugin.HaloReactActivity

class MainActivity : HaloReactActivity() {

    override fun getMainComponentName(): String = "MyHaloApp"

    override fun createReactActivityDelegate() =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
```

This replaces `ReactActivity` so that NFC foreground dispatch and the Halo SDK lifecycle are managed automatically.

### AndroidManifest Permissions

**6.** Add the required permissions to `android/app/src/main/AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <!-- Bluetooth — legacy permissions for API 29/30 -->
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

    <!-- Bluetooth — API 31+ -->
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN"
        android:usesPermissionFlags="neverForLocation" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />

    <!-- Location (required for Bluetooth LE scanning) -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />

    <!-- Other -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.NFC" />

    <uses-feature android:name="android.hardware.nfc" android:required="true" />

    <!--
        tools:replace is required because the Halo SDK (and its bundled Visa library)
        declare android:label and android:allowBackup in their own manifests.
        Without these overrides the manifest merger will refuse to build.
    -->
    <application
        ...
        tools:replace="android:label,android:allowBackup">
        <activity
            android:name=".MainActivity"
            ...>
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

            <!-- Required: NFC foreground dispatch -->
            <intent-filter>
                <action android:name="android.nfc.action.TECH_DISCOVERED" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## JWT — What It Is and How to Set It Up

Every call to the Halo SDK must include a valid JWT. The SDK requests one via the `onRequestJWT` callback whenever it needs to authenticate.

A JWT must be obtained from your backend or generated using your RSA private key and the credentials from the [developer portal](https://go.developerportal.qa.haloplus.io/). See the [Halo developer documentation](https://halo-dot-developer-docs.gitbook.io/halo-dot/sdk) for the required claims. For testing, paste a valid token directly into `Config.tempJwt` as shown below.

Split your config and JWT supply into two files:

**`src/config.ts`** — stores your app settings (never commit real values to source control):

```typescript
export const Config = {
  // Paste a valid JWT here for testing.
  // Regenerate it if the SDK reports JWTExpired on startup.
  tempJwt: 'eyJ...',

  // Obtained from the developer portal
  applicationPackageName: 'com.yourcompany.myapp',
  applicationVersion: '1.0.0',
  onStartTransactionTimeOut: 300000,  // ms before "tap card" times out
  enableSchemeAnimations: true,       // show Visa/Mastercard animations
} as const;
```

**`src/jwt/JwtToken.ts`**: supplies the JWT when the SDK asks for one:

```typescript
import { Config } from '../config';

export function getJwt(): string {
  if (Config.tempJwt) {
    return Config.tempJwt;
  }
  throw new Error('No JWT configured. Set Config.tempJwt in src/config.ts.');
}
```

> **Important:** Add `config.ts` to `.gitignore` so you never accidentally commit a real token to source control.

---

## Usage

### Step 1 — Request Permissions

Request the Android runtime permissions before initialising the SDK. Android 12+ (API 31+) uses new Bluetooth permission names.

```typescript
// src/permissions.ts
import { PermissionsAndroid, Platform } from 'react-native';

export async function requestHaloPermissions(): Promise<void> {
  if (Platform.OS !== 'android') return;

  const sdkVersion =
    typeof Platform.Version === 'number'
      ? Platform.Version
      : parseInt(Platform.Version, 10);

  const permissions: string[] = [
    PermissionsAndroid.PERMISSIONS.CAMERA,
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
  ];

  if (sdkVersion >= 31) {
    // Android 12+ Bluetooth permissions
    permissions.push(
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
    );
  }

  await PermissionsAndroid.requestMultiple(permissions);
}
```

### Step 2 — Set Up Callbacks

The SDK communicates back to your app through an `IHaloCallbacks` object you provide. Each callback corresponds to a different type of event.

```typescript
// src/haloCallbacks.ts
import {
  type IHaloCallbacks,
  type HaloAttestationHealthResult,
  type HaloInitializationResult,
  type HaloTransactionResult,
  type HaloUIMessage,
} from 'halo-sdk-react-native';
import { getJwt } from './jwt/JwtToken';

export function buildCallbacks(options: {
  onStatusChange: (msg: string) => void;
  onError: (msg: string) => void;
  onTransactionResult: (result: HaloTransactionResult) => void;
}): IHaloCallbacks {
  return {
    // Called when the SDK finishes starting up.
    // Check resultType === 'Initialized' for successful init.
    onInitializationResult(result: HaloInitializationResult) {
      if (result.resultType === 'Initialized') {
        options.onStatusChange('SDK ready — present a card to pay');
      } else {
        options.onError(`Initialisation failed: ${result.resultType} (${result.errorCode})`);
      }
    },

    // Called when a card tap completes (approved, declined, or error)
    onHaloTransactionResult(result: HaloTransactionResult) {
      options.onTransactionResult(result);
    },

    // Called repeatedly during a transaction to tell you what to show the user
    // e.g. "PRESENT_CARD", "PROCESSING", "APPROVED"
    onHaloUIMessage(message: HaloUIMessage) {
      options.onStatusChange(message.msgID);
    },

    // The SDK asks you for a fresh JWT whenever it needs one
    onRequestJWT(jwtCallback: (jwt: string) => void) {
      try {
        jwtCallback(getJwt());
      } catch (err: any) {
        options.onError(`JWT error: ${err.message}`);
      }
    },

    // Device failed attestation (tampered OS, emulator, etc.)
    onAttestationError(details: HaloAttestationHealthResult) {
      options.onError(`Attestation error: ${details.errorCode}`);
    },

    // A security check failed (e.g. invalid JWT, revoked merchant)
    onSecurityError(errorCode: string) {
      options.onError(`Security error: ${errorCode}`);
    },

    // The SDK released the camera (e.g. card scheme animation finished)
    onCameraControlLost() {
      console.log('Camera released by SDK');
    },
  };
}
```

### Step 3 — Initialize the SDK

Call `HaloSdk.initialize` once, before running any transactions. A good place is in a `useEffect` when your payment screen mounts.

```typescript
import { HaloSdk } from 'halo-sdk-react-native';
import { Config } from './config';
import { requestHaloPermissions } from './permissions';
import { buildCallbacks } from './haloCallbacks';

async function setupSdk(
  onStatusChange: (msg: string) => void,
  onError: (msg: string) => void,
  onTransactionResult: (result: any) => void,
): Promise<void> {
  // 1. Request permissions first
  await requestHaloPermissions();

  // 2. Build your callbacks
  const callbacks = buildCallbacks({ onStatusChange, onError, onTransactionResult });

  // 3. Initialise — the SDK will call onInitializationResult when ready
  await HaloSdk.initialize(
    callbacks,
    Config.applicationPackageName,  // e.g. 'com.yourcompany.myapp'
    Config.applicationVersion,      // e.g. '1.0.0'
    Config.onStartTransactionTimeOut, // ms before a tap times out (default 300000)
    Config.enableSchemeAnimations,    // show Visa/Mastercard animations
  );
}
```

### Step 4 — Run a Transaction

Once the SDK is initialised, you can charge a card:

```typescript
// Charge R 10.50
const result = await HaloSdk.startTransaction(10.50, 'ORDER-001', 'ZAR');
console.log(result.resultType); // e.g. "tap_started"

// Refund R 10.50 to the original card
const refund = await HaloSdk.cardRefundTransaction(10.50, 'ORDER-001', 'ZAR');

// Cancel a transaction that is waiting for a tap
await HaloSdk.cancelTransaction();
```

`startTransaction` and `cardRefundTransaction` resolve immediately once the tap is registered, the final payment outcome arrives through `onHaloTransactionResult`.

### Full Example

Below is a minimal but complete payment screen taken directly from the example app in this repo:

```tsx
// App.tsx
import { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {
  HaloSdk,
  type HaloTransactionResult,
  type IHaloCallbacks,
} from 'halo-sdk-react-native';
import { getJwt } from './src/jwt/JwtToken';
import { Config } from './src/config';
import { requestHaloPermissions } from './src/permissions';

export default function App() {
  const [amount, setAmount] = useState('');
  const [merchantRef, setMerchantRef] = useState('');
  const [status, setStatus] = useState('Initialising...');
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    initSdk();
  }, []);

  async function initSdk() {
    await requestHaloPermissions();

    const callbacks: IHaloCallbacks = {
      onInitializationResult(result) {
        setIsReady(result.resultType === 'Initialized');
        setStatus(
          result.resultType === 'Initialized'
            ? 'Ready — enter amount and tap Charge'
            : `Init failed: ${result.resultType} (${result.errorCode})`,
        );
      },
      onHaloTransactionResult(result: HaloTransactionResult) {
        setStatus(`Result: ${result.resultType} ${result.errorCode}`);
      },
      onHaloUIMessage(message) {
        // The SDK sends messages like "PRESENT_CARD", "PROCESSING", "APPROVED"
        setStatus(message.msgID);
      },
      onRequestJWT(jwtCallback) {
        try {
          jwtCallback(getJwt());
        } catch (e: any) {
          setStatus(`JWT error: ${e.message}`);
        }
      },
      onAttestationError(details) {
        setStatus(`Attestation error: ${details.errorCode}`);
      },
      onSecurityError(errorCode) {
        setStatus(`Security error: ${errorCode}`);
      },
      onCameraControlLost() {},
    };

    HaloSdk.initialize(
      callbacks,
      Config.applicationPackageName,
      Config.applicationVersion,
      Config.onStartTransactionTimeOut,
      Config.enableSchemeAnimations,
    ).catch(e => setStatus(`SDK error: ${e.message}`));
  }

  async function charge() {
    if (!amount || !merchantRef) {
      setStatus('Please enter amount and merchant reference');
      return;
    }
    try {
      const result = await HaloSdk.startTransaction(parseFloat(amount), merchantRef, 'ZAR');
      setStatus(`Tap accepted: ${result.resultType}`);
    } catch (e: any) {
      setStatus(`Error: ${e.message}`);
    }
  }

  return (
    <View style={{ flex: 1, padding: 24, justifyContent: 'center' }}>
      <Text style={{ fontSize: 13, color: '#555', marginBottom: 16 }}>{status}</Text>

      {!isReady ? (
        <ActivityIndicator size="large" />
      ) : (
        <>
          <TextInput
            placeholder="Amount (e.g. 10.50)"
            value={amount}
            onChangeText={setAmount}
            keyboardType="decimal-pad"
            style={{ borderWidth: 1, borderColor: '#ccc', padding: 8, marginBottom: 8, borderRadius: 4 }}
          />
          <TextInput
            placeholder="Merchant reference (e.g. ORDER-001)"
            value={merchantRef}
            onChangeText={setMerchantRef}
            style={{ borderWidth: 1, borderColor: '#ccc', padding: 8, marginBottom: 16, borderRadius: 4 }}
          />
          <TouchableOpacity
            onPress={charge}
            style={{ backgroundColor: '#1976D2', padding: 14, borderRadius: 8, alignItems: 'center' }}>
            <Text style={{ color: '#fff', fontSize: 16, fontWeight: '600' }}>Charge</Text>
          </TouchableOpacity>
        </>
      )}
    </View>
  );
}
```

---

## API Reference

### `HaloSdk.initialize(callbacks, packageName, version, timeout?, animations?)`

Initialises the SDK. Must be called before any transaction methods.

| Parameter | Type | Default | Description |
|---|---|---|---|
| `callbacks` | `IHaloCallbacks` | — | Your callback handler object |
| `applicationPackageName` | `string` | — | Your app's Android package name |
| `applicationVersion` | `string` | — | Your app's version string |
| `onStartTransactionTimeOut` | `number?` | `300000` | Time in ms to wait for a card tap |
| `enableSchemeAnimations` | `boolean?` | `false` | Show Visa/Mastercard/Amex animations on approval |

### `HaloSdk.startTransaction(amount, reference, currency)`

Starts a purchase transaction. Prompts the user to tap their card.

| Parameter | Type | Example |
|---|---|---|
| `transactionAmount` | `number` | `10.50` |
| `merchantTransactionReference` | `string` | `'ORDER-001'` |
| `transactionCurrency` | `string` | `'ZAR'` |

Returns `Promise<HaloStartTransactionResult>` — resolves when the card tap is registered. The final outcome arrives via `onHaloTransactionResult`.

### `HaloSdk.cardRefundTransaction(amount, reference, currency)`

Starts a card-present refund. Same parameters as `startTransaction`.

### `HaloSdk.cancelTransaction()`

Cancels the current in-progress transaction (e.g. if the user presses Cancel while waiting for a tap).

### Callbacks (`IHaloCallbacks`)

| Callback | When it fires |
|---|---|
| `onInitializationResult(result)` | SDK startup complete (success or failure) |
| `onHaloTransactionResult(result)` | Final payment outcome; approved, declined, or error |
| `onHaloUIMessage(message)` | Prompt to show the user during a tap: `PRESENT_CARD`, `PROCESSING`, `APPROVED`, etc. |
| `onRequestJWT(jwtCallback)` | SDK needs a fresh JWT; call `jwtCallback(yourJwtString)` |
| `onAttestationError(details)` | Device integrity check failed (rooted device, emulator, etc.) |
| `onSecurityError(errorCode)` | JWT invalid, merchant revoked, or other security failure |
| `onCameraControlLost()` | SDK has finished using the camera |

### Result Types

`resultType` and `errorCode` are plain strings serialised from native Android enums. Use these known values in your conditional logic.

**`HaloInitializationResult.resultType`**

| Value | Meaning |
|---|---|
| `'Initialized'` | SDK ready; safe to call `startTransaction` |
| `'RemoteAttestationFailure'` | Cloud attestation failed; inspect `errorCode` for the reason |
| `'LocalAttestationFailure'` | Device integrity check failed (rooted device, emulator, etc.) |

**`HaloInitializationResult.errorCode`** (when `resultType !== 'Initialized'`)

| Value | Meaning |
|---|---|
| `'OK'` | No error — accompanies `resultType: 'Initialized'` |
| `'JWTExpired'` | The JWT has expired — generate a fresh one |


**`HaloTransactionResult.resultType`**

| Value | Meaning |
|---|---|
| `'Approved'` | Transaction approved by the payment network |
| `'Declined'` | Card declined |
| `'Cancelled'` | Transaction cancelled (e.g. via `cancelTransaction()`) |
| `'Error'` | An error occurred — inspect `errorCode` and `errorDetails` |

---

## Documentation

Full SDK documentation: [halo-dot-developer-docs.gitbook.io](https://halo-dot-developer-docs.gitbook.io/halo-dot/sdk)

---

## Testing

All transactions are void until your NDA is fully executed.

You can simulate card taps using a virtual NFC card app such as [Visa Mobile CDET](https://apkpure.com/visa-mobile-cdet/com.visa.app.cdet) on a second Android device.

---

## FAQ

**Q: How do I set `compileSdkVersion` / `minSdkVersion` if they're not set or causing issues?**

You can define them in `android/local.properties` and read them in Gradle:

```properties
sdk.dir=C\:\\Users\\yourname\\Library\\Android\\Sdk
aws.accesskey=YOUR_ACCESS_KEY
aws.secretkey=YOUR_SECRET_KEY
compileSdkVersion=34
minSdkVersion=29
```

```gradle
// android/app/build.gradle
compileSdkVersion localProperties.getProperty('compileSdkVersion').toInteger()
```

---

**Q: The SDK fails to download / Gradle sync fails.**

- Confirm `aws.accesskey` and `aws.secretkey` are in `android/local.properties` with the exact casing shown
- Open the `android` folder in Android Studio and run **File → Sync Project with Gradle Files**
- Make sure you accepted the NDA on the developer portal (access is blocked until the NDA is signed)

---

**Q: I get a build error about `HaloReactActivity` or `HaloSdkPackage` not found.**

- Confirm the npm package is installed: `npm install halo-sdk-react-native`
- Confirm `MainActivity` extends `HaloReactActivity` (not `ReactActivity`)
- Run a Gradle sync in Android Studio

---

**Q: The SDK initialises but transactions never complete.**

- Check that all [AndroidManifest permissions](#androidmanifest-permissions) are declared, especially the NFC `intent-filter` block
- Check that the NFC intent-filter is inside the `<activity>` block for `MainActivity`
- Verify your JWT is valid using `POST https://kernelserver.qa.haloplus.io/<sdk-version>/tokens/checkjwt`
- Check that `minSdkVersion` ≥ 29 and `compileSdkVersion` / `targetSdkVersion` ≥ 34

---

**Q: How do I get a JWT for testing?**

A JWT must be generated using your RSA private key and the credentials from the [developer portal](https://go.developerportal.qa.haloplus.io/). See the [Halo developer documentation](https://halo-dot-developer-docs.gitbook.io/halo-dot/sdk) for the required claims and signing algorithm. Once you have a valid token, paste it into `Config.tempJwt` in your `config.ts`.

---

**Q: Manifest merger fails with an attribute conflict (e.g. `android:label`, `android:allowBackup`).**

The Halo SDK bundles several sub-libraries (Visa Sensory Branding, etc.), each with their own `AndroidManifest.xml`. Any `processDebugMainManifest` failure caused by an attribute clash is fixed by adding the conflicting attribute name to `tools:replace` on your `<application>` element:

```xml
<application
    ...
    tools:replace="android:label,android:allowBackup">
```

If you add a new attribute to `tools:replace` and the **same error persists on the very next build**, Gradle may have cached the previously failed manifest merge. Run a clean build and try again:

```bash
cd android && ./gradlew clean
```

Then re run your normal build (`npx react-native run-android` or Android Studio).

---

**Q: TypeScript build errors about `customConditions` or `moduleResolution` after editing `tsconfig.json`.**

Do not override `moduleResolution` in your project's `tsconfig.json`. The base `@react-native/typescript-config` sets `"moduleResolution": "bundler"`, which is the only value compatible with its `customConditions` setting. Overriding it to `"node"` causes a TypeScript error.

Instead, extend the base config and only add project specific overrides:

```json
{
  "extends": "@react-native/typescript-config/tsconfig.json",
  "compilerOptions": {
    "skipLibCheck": true
  }
}
```

`skipLibCheck: true` suppresses spurious type errors that originate inside `node_modules` (e.g. phantom `@types/react` v19 conflicts) without changing how your own code is compiled.

---

**Q: `onInitializationResult` fires with `resultType: 'RemoteAttestationFailure'` and `errorCode: 'JWTExpired'`.**

Your temp JWT has expired. Developer-portal tokens are short-lived (typically 15 minutes). This is expected behaviour — the SDK fires a failure callback with the cached/expired token, then automatically requests a new JWT via `onRequestJWT` and retries. If `onInitializationResult` fires a **second time** shortly after with `resultType: 'Initialized'`, everything is working correctly.

If the second successful callback never arrives:
1. Generate a fresh token and update `Config.tempJwt`
2. Make sure your `onRequestJWT` callback calls `jwtCallback(yourJwt)` synchronously — async calls are not supported without extra handling
