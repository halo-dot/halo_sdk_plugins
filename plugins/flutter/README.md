# Halo SDK Flutter Plugin

A flutter implementation of the Halo SDK.

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

## Getting started for users

### Environment
1. Make sure you have your envirnoment setup to buidl Flutter apps. You can follow the instructions [here](https://flutter.dev/docs/get-started/install).

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

Check also in your `android/app/build.gradle` file. You should have something like this:
```gradle
ext {
    kotlin_version = '1.3.72' // <-- version defined here
}
// ...
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version" // <-- version used here
    // ...
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

1. In your `pubspec.yaml` file, add the following dependency:
(Once it's live)
```yaml
dependencies:
  sdkflutterplugin: ^0.0.1
```
(For now)
```yaml
dependencies:
  sdkflutterplugin: ../sdkflutterplugin # <-- relative path to the plugin root folder
```

2. The plugin will need to download the SDK binaries from the Halo S3 bucket. To do this, you will need credentials to access the SDK. Find your `accessKeyId` and `secretAccessKey` [here](https://go.developerportal.dev.haloplus.io/). Add these to your `local.properties` file in your project android root folder (create one if it doesn't exist):

```properties
aws.accesskey=ABCEFGHIJKLMNOPOOO
aws.secretkey=F1Gb2024LHoX44WEWUvaL70I0luATf5Vqqc983gNP3BA
```

3. Then add this to you `android/app/build.gradle` file:

```gradle
def localProperties = new Properties()
def localPropertiesFile = rootProject.file('local.properties')
if (localPropertiesFile.exists()) {
    localPropertiesFile.withReader('UTF-8') { reader ->
        localProperties.load(reader)
    }
}
```

4. Finally set the repo for kotlin `1.3.72` and some more config in the `android/build.gradle` file:
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

### Usage

1. First you need to request the permissions needed by the SDK. Add the following permissions to your `AndroidManifest.xml` file:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="za.co.synthesis.halo.sdkflutterplugin_example">
    <uses-permission android:name="android.permission.INTERNET"/><!--  add this  -->
    <uses-permission android:name="android.permission.NFC"/><!--  add this  -->
    <!--  ....  -->
</manifest xmlns:android="http://schemas.android.com/apk/res/android">
```
2. Request the permissions in your Flutter application before initializing the SDK. Here is an example of how to do this:

```dart
```

3. Your Android `MainActivity` class should extend `HaloActivity` which hooks into the SDK lifecycle methods for you. (FYI: `HaloActivity` extends `FlutterFragmentActivity`).

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

NB: if you do not get a UI message back, it's likey the JWT is invalid or expired.

From this point, a number of UI messages will be pushed to the registered callbacks.
You will use this to show the user the appropriate UI/text.



## Getting started for developers

### Environment

See [Environment](#environment) above.

### Things to know

- The codebase uses MethodChannels to trigger native functions from the Flutter side.
- Although we could have used MethodChannels to send callback messages from native code to Flutter, we use EventChannels (no particular reason, just a tried and tested method)
- The Channels that pass data between the two platforms accepts a limited list of data types.
- Importtant to note that Lists and Maps should always be of type `dynamic` when passing data from native to dart.
- Although little documentation exists about EventChannels, they too follow most of the rules of MethodChannels.

Read all about it [here](https://docs.flutter.dev/platform-integration/platform-channels).

### Plugin Development

- The example app will build the code from the plugin folder, no need to build the plugin separately.
