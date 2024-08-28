# Halo SDK Flutter Plugin Test App

This is a test app for the Halo SDK Flutter plugin. It demonstrates how to use the plugin to integrate Halo SDK into a Flutter app.

To view the SDK readme, follow this [link](../../plugins/flutter/README.md)

## Getting Started

- The example app was tested using Java 11. We cannot confirm yet if anything before or later will work.

- The example app was tested using Flutter `2.10.5` and Dart `2.16.2` (DevTools `2.9.2`).

1. Clone the repository

2. Open the `test_apps/flutter` directory with your IDE.

3. Add the following to your `test_apps/flutter/android/local.properties` file:
```
aws.accesskey=<accesskey>
aws.secretkey=<secretkey>
```

4. In `test_apps/flutter` run `flutter pub get` to install the dependencies.

5. For testing purposes, you can add your JWT config to `/lib/config.dart` so that we generate a JWT token for you. If you want to use an already issued JWT, add it in the `/lib/jwt_token.dart` file as the `tempJwt` variable.

6. Run the app using `flutter run` or your IDE's run command.
