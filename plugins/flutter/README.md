# Halo SDK Flutter Plugin

A flutter implementation of the [Halo Dot SDK](https://halo-dot-developer-docs.gitbook.io/halo-dot/sdk/1.-getting-started).

The Halo Dot SDK is an Isolating MPoC SDK payment processing MPOC Software with Attestation & Monitoring Capabilities.

The Architecture of this isolating MPoC Payment Software, is described in the diagram below.

The below diagram also showcases the SDK boundary and the interaction between the SDK its integrating channels, and the 3rd party payment gateway. It also describes the boundary of the SDK and how it interacts with integrators and the third party payments. It also includes details of how the data is communicated sent in-between the boundary.

![Halo Dot SDK Architecture](./readme_assets/halo_sdk_architecture.png)

## Getting started

### Environment
1. Make sure you have your environment setup to build Flutter apps. You can follow the instructions [here](https://flutter.dev/docs/get-started/install).

2. The Android SDK that is implemented was built with kotlin `1.3.72`, your project should be on the same version (>= `1.4.x` has breaking changes).

Check this in your `android/build.gradle` file. You should have something like this:

```gradle
ext {
    kotlin_version = '1.3.72' // <-- version defined here
}
buildscript {
    dependencies {
        // ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version" // <-- version used here
    }
}
```

3. The SDK was tested using Java 11. We cannot confirm yet if anything before or later will work.

4. The SDK was tested using Flutter `2.10.5` and Dart `2.16.2` (DevTools `2.9.2`).

5. The `minSdkVersion` for the Android project should be `29` or higher. Check this in your `android/app/build.gradle` file:
```gradle
defaultConfig {
    applicationId "za.co.synthesis.halo.sdkflutterplugin_example"
    minSdkVersion 29 // <-- this should be 29 or higher
    // ...
}
```

### Plugin Installation

1. Run `flutter pub add halo_sdk_flutter_plugin` to add the flutter plugin to your flutter project

2. The plugin will need to download the SDK binaries from the Halo S3 bucket. To do this, you will need credentials to access the SDK. Find your `accesskey` and `secretkey` [here](https://go.developerportal.dev.haloplus.io/). Add these to your `local.properties` file in your android root folder (create one if it doesn't exist):

```properties
aws.accesskey=<accesskey>
aws.secretkey=<secretkey>
```

<strong>NB: mind the casing of the values</strong>

3. Then add this to you `android/app/build.gradle` file (this might already exist):

```gradle
def localProperties = new Properties()
def localPropertiesFile = rootProject.file('local.properties')
if (localPropertiesFile.exists()) {
    localPropertiesFile.withReader('UTF-8') { reader ->
        localProperties.load(reader)
    }
}
```

4. Finally make sure you have Maven Central for kotlin `1.3.72` and some more config in the `android/build.gradle` file:
```gradle
allprojects {
    repositories {
        google()
        mavenCentral() // <-- add this for kotlin 1.3.72
        maven { url 'https://jitpack.io' }

        // add this block
        configurations.all {
            resolutionStrategy.cacheChangingModulesFor 1, 'days'
            resolutionStrategy.dependencySubstitution {
                substitute(module("androidx.core:core-ktx")).with(module("androidx.core:core-ktx:(*, 1.3.2]"))
                substitute(module("org.jetbrains.kotlin:kotlin-stdlib-jdk7")).with(module("org.jetbrains.kotlin:kotlin-stdlib-jdk7:(*, 1.3.72]"))
                substitute(module("org.jetbrains.kotlin:kotlin-stdlib-jdk8")).with(module("org.jetbrains.kotlin:kotlin-stdlib-jdk7:(*, 1.3.72]"))
                substitute(module("androidx.window:window-java")).with(module("androidx.core:core-ktx:(*, 1.3.2]"))
                substitute(module("com.google.firebase:firebase-analytics-ktx")).with(module("com.google.firebase:firebase-analytics-ktx:19.0.0"))
            }
        }
    }
    // ...
}
```

### Requirements on the Mobile Back-End

#### JWT

All calls made to the Halo.SDK requires a valid JWT. The mobile application's server should issue the JWT that can be used to authenticate with the Halo Kernel Server.

You should provide this JWT to the callback function when `onRequestJWT` is invoked by the Halo Dot SDK.

#### JWT LifeTime

Since the JWT essentially authorizes payment acceptance for a given merchant user, it is essential that the JWT lifetime be kept as short as possible, in order to limit the amount of time an attacker has to crack the key itself and then to limit the scope of damage in the event of a key compromise.

A lifetime of 15 minutes is recommended.

#### JWT Signing Public Key Format

The JWT public key should be published as a certificate, in a text-friendly format, e.g. B64 encoded PEM (.crt, .pem).

#### JWT Serialization Format

The compact serialization format is expected, i.e:

```
urlencodedB64(header) + '.' + urlencodedB64(payload) + '.' + urlencodedB64(signature)
```

For example:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2Mzh9.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyH
```

#### JWT Claims

The JWT must make a number of claims - all of them standard except for `aud_fingerprints` (Audience Fingerprints):

| field |	type |	Note |
| ---- | ---- | ---- |
| alg | String |The signing algorithm is RSA signed SHA-256 hash, aliased as RS256. An asymmetric encryption(signing) scheme is required to allow the Kernel Server to be able to validate the token without being able to generate it. If symmetric key encryption was used to sign the auth token (e.g., using the HMAC algorithm), then non-repudiation would be lost. |
| sub | String | The Payment Processor Merchant-User ID, or Application ID |
| iss | String | This is a unique (from the perspective of Halo server) identifier for the JWT issuer, agreed upon by the JWT issuer and Synthesis, and configured in advance by Synthesis in the Halo server, e.g. authserver.haloplus.io |
| aud | String | URL of Halo server TLS endpoint, e.g. 'kernelserver.qa.haloplus.io'. This value should be obtained from Synthesis (different per environment) e.g. for QA it would be 'kernelserver.qa.haloplus.io' and for DEV 'kernelserver.za.dev.haloplus.io' |
| usr | String | The details of the user performing the transaction, typically the username used to sign into the Integrators application. |
| iat | NumericDate | The UTC timestamp of when the JWT was generated. |
| exp | NumericDate | The UTC time of expiration of the JWT. |
| aud_fingerprints | String | a CSV list of expected SHA-256 fingerprints for the Kernel Server TLS endpoint. This list may contain multiple values to support certificate rotation. In the QA environment, the expected value as of writting this would be: "sha256/zc6c97JhKPZUa+rIrVqjknDE1lDcDK77G41sDo+1ay0=" |

All these values can be validated by making a request to `https://kernelserver.qa.haloplus.io/<sdk-version>/tokens/checkjwt`. 
</br>Method: `POST`
</br>Header: Bearer Auth

### Usage

1. First you need to request the permissions needed by the SDK. Add the following permissions to your `AndroidManifest.xml` file:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="za.co.synthesis.halo.sdkflutterplugin_example">
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.NFC"/>
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    <!--  ....  -->
</manifest xmlns:android="http://schemas.android.com/apk/res/android">
```

2. Add the plugin to help you request permissions in your Flutter application. Add the following to your `pubspec.yaml` file:
```yaml
dependencies:
  permission_handler: ^11.0.0
  # ...
```
Notice the version, later versions expect a higher version of Flutter and Dart. Also if you use this plugin. Be sure in your `android/app/build.gradle` your `compileSdkVersion` and `targetSdkVersion` are at `34` or higher.

<strong>Remember to run `flutter pub get` after making changes to your pubspec file</strong>

3. Request the permissions in your Flutter application before initializing the SDK. Here is an example of how to do this (please handle cases where the user denies the permissions or rational is requested):

```dart
Future<void> checkPermissions() async {
  var permissions = [
    Permission.camera,
    Permission.phone,
    Permission.storage,
    Permission.notification,
    Permission.location,
  ];

  for (var permission in permissions) {
    await requestPermission(permission);
  }
}

Future<void> requestPermission(Permission permission) async {
  var permissionStatus = await permission.status;
  if (permissionStatus.isGranted) {
    debugPrint("${permission} permission is granted, not requesting");
  } else if (permissionStatus.isPermanentlyDenied) {
    debugPrint("${permission} permission is permanently denied, enable in settings");
  } else {
    var requestResult = await permission.request();

    if (requestResult.isGranted) {
      debugPrint("${permission} permission is granted");
    } else if (requestResult.isPermanentlyDenied) {
      debugPrint("${permission} permission is permanently denied, enable in settings");
    }
  }
}
```

3. Your Android `MainActivity` class should extend `HaloActivity` which hooks into the SDK lifecycle methods for you. (FYI: `HaloActivity` extends `FlutterFragmentActivity`).

e.g
```kotlin
import za.co.synthesis.halo.sdkflutterplugin.HaloActivity

class MainActivity: HaloActivity() {
}
```

4. In your Flutter project, you can now use the plugin to interact with the SDK. Here is an example of how to use the plugin:

- First you have to implement the `IHaloCallbacks` interface in your Flutter project. This will allow you to receive callbacks from the SDK. Here is an example of how to do this:

```dart
class HaloCallbacks implements IHaloCallbacks {
  @override
  void onAttestationError(HaloAttestationHealthResult details) {
    debugPrint("example app: attestation error: $details");
  }

  @override
  void onHaloTransactionResult(HaloTransactionResult result) {
    debugPrint("example app: transaction result: $result");
  }

  @override
  void onHaloUIMessage(HaloUIMessage message) {
    debugPrint("example app: UI message: $message");
  }

  @override
  void onInitializationResult(HaloInitializationResult result) {
    debugPrint("example app: initialization message: $result");
  }

  @override
  void onRequestJWT(void Function(String jwt) callback) {
    debugPrint("example app: onRequestJWT");
    // get the jwt and pass it to the callback function
    callback(jwt);
  }

  @override
  void onSecurityError(errorCode) {
    debugPrint("example app: security error: $errorCode");
  }
}
```
You can decide, based on what the SDK sends via the callbacks, how you would like to affect the UI

- Next you want to initialize the SDK, ideally when the Widget that will handle the transaction is opened. Here is an example of how to do this:
```dart
Sdkflutterplugin.initializeHaloSDK(haloCallbacks,
        "za.co.synthesis.halo.sdkflutterplugin_example", "0.0.1");
```

- Next you can start a transaction. Here is an example of how to do this:
```dart
Sdkflutterplugin.startTransaction(1.00, 'Some merchant reference', 'ZAR');
```

From this point, a number of UI messages will be pushed to the registered callbacks.
You will use this to show the user the appropriate UI/text.
