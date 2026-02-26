# Halo RN Example

React Native test app for the `halo-sdk-react-native` plugin. Demonstrates SDK initialisation, purchase transactions, card refunds, and cancellation on Android.

## Prerequisites

- Node.js 18+
- React Native CLI (`npm install -g @react-native-community/cli`)
- Android Studio with SDK 29+ installed
- An NFC-capable Android device or emulator
- AWS credentials for the Halo Maven S3 repository

## Setup

### 1. Install dependencies

Ensure that you at the root directory before you run the following.

```bash
npm install
```

### 2. Configure the JWT

The app uses a JWT to authenticate with the Halo backend. Open [src/config.ts](src/config.ts) and fill in your credentials:

```typescript
export const Config = {
  // Option A: paste a short-lived JWT directly, this is easier actually.
  tempJwt: 'eyJ...',

  // Option B: provide a private key to generate JWTs at runtime
  privateKeyPem: '',
  issuer: 'your-issuer',
  username: '+27771234567',
  merchantId: 'your-merchant-id',
  host: 'kernelserver.za.dev.haloplus.io',
};
```

### 3. Configure Android AWS credentials

The plugin fetches the Halo SDK from a private S3 Maven repository. Create `android/local.properties` with:

```properties
aws.accesskey=YOUR_ACCESS_KEY
aws.secretkey=YOUR_SECRET_KEY
aws.token=YOUR_SESSION_TOKEN   # optional, for temporary credentials
```

Or export the equivalent environment variables before building:

```bash
export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
export AWS_SESSION_TOKEN=...
```

## Running

Connect an Android device (with NFC) or start an emulator, then:

```bash
# Start Metro bundler
npm start

# In a separate terminal, build and install
npx react-native run-android
```

After the app is built, ensure to check if it has the correct permissions in the app's settings on your device.
If it does not, please give it those permissions manually.

## App Overview

The app presents a simple payment terminal UI:

- **Amount (ZAR)** — decimal amount field
- **Merchant Reference** — unique reference for the transaction
- **Charge** — initiates a purchase tap
- **CP Refund** — initiates a card-present refund tap
- **Cancel** — cancels the current in-progress transaction

On launch the app requests camera permission and initialises the Halo SDK. Status and result messages stream into a log panel. A spinner is shown while the SDK is initialising.

## Project Structure

```
test_apps/react-native/
├── android/                  # Android host app
│   └── app/src/main/kotlin/  # MainActivity (extends HaloReactActivity)
├── src/
│   ├── config.ts             # JWT / environment configuration
│   ├── jwt/JwtToken.ts       # JWT generation helper
│   └── components/           # UI components (Keypad, AmountDisplay)
├── App.tsx                   # Root component
└── index.js                  # Entry point
```

## Key Integration Points

`android/app/src/main/kotlin/.../MainActivity.kt` extends `HaloReactActivity` (from the plugin) instead of the standard `ReactActivity`. This is required for NFC foreground dispatch and SDK lifecycle management.
