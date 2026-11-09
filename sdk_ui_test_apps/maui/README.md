# HaloSdkUi.Example

A .NET MAUI test app for the [`HaloDot.SdkUi.Maui`](https://www.nuget.org/packages/HaloDot.SdkUi.Maui) plugin, which takes payments through Halo's ready-made payment screens (`sdk_ui`).

Calls the plugin from one screen, with every result shown on it. The whole integration is one line, `.UseHaloSdkUi()`, in [`MauiProgram.cs`](HaloSdkUi.Example/MauiProgram.cs).

## Getting up and running

You need to have .NET 10 with the `maui-android` workload, JDK 21, the Android SDK, and an NFC-capable Android phone with USB debugging on.

1. **Get your Halo values.** On the [Halo Developer Portal](https://developerportal.dev.haloplus.io/), add your issuer. The portal downloads your private key and shows your AWS keys and a `Config` block.
2. **Add your AWS keys** to `local.properties` in this folder. Add `aws.token` only if your keys are temporary ones:

   ```properties
   aws.accesskey=<your access key>
   aws.secretkey=<your secret>
   aws.token=<your token>
   ```

3. **Add your private key.** You will get this from the [Halo Developer Portal](https://developerportal.dev.haloplus.io/). Save it as `HaloSdkUi.Example/private_key.pem`.
4. **Add your details.** Copy [`Config.example.cs`](HaloSdkUi.Example/Config.example.cs) to a new file `Config.cs` in the same folder, and fill in each value from the portal's `Config` block:

   | Portal        | `Config.cs`  |
   | ------------- | ------------ |
   | `ISSUER`      | `Issuer`     |
   | `USERNAME`    | `Username`   |
   | `MERCHANT_ID` | `MerchantId` |
   | `HOST`        | `Host`       |
   | `AUD`         | `Aud`        |
   | `KSK`         | `Ksk`        |

5. **Run it** from this folder, with your phone connected:

   ```bash
   dotnet build HaloSdkUi.Example -t:Run -f net10.0-android
   ```

6. **Allow the permissions** the app asks for. When the SDK is up, the screen shows **SDK ready**. If it shows an error on the first start, close and reopen the app once the permissions are allowed.

`.gitignore` keeps `Config.cs`, `private_key.pem` and `local.properties` out of git. The key is only built into Debug builds, so Release builds can't start the SDK. A real host signs its JWTs on its own backend.

## The brand file

[`brand.json`](HaloSdkUi.Example/Platforms/Android/Assets/halo/brand.json) is what the SDK reads its appearance and addressing from. See **Branding** in the [plugin's README](https://www.nuget.org/packages/HaloDot.SdkUi.Maui).

Telemetry is on in that file so a run shows what a host gets. This app ships no `google-services.json`, so the Firebase provider finds no project and retires itself. Every signal still lands in logcat:

```bash
adb logcat -s HDTelemetry
```
