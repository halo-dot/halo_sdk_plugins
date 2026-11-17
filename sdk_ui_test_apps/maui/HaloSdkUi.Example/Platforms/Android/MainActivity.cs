using Android.App;
using Android.Content;
using Android.Content.PM;
using Android.Nfc;

namespace HaloDot.SdkUi.Example;

// singleTop so a Zebra Print Touch tap lands in this Activity, where the SDK resolves the printer.
[Activity(Theme = "@style/Maui.SplashTheme", MainLauncher = true, LaunchMode = LaunchMode.SingleTop, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize | ConfigChanges.Density)]
[IntentFilter([NfcAdapter.ActionNdefDiscovered], Categories = [Intent.CategoryDefault], DataSchemes = ["http", "https"])]
public class MainActivity : MauiAppCompatActivity
{
}
