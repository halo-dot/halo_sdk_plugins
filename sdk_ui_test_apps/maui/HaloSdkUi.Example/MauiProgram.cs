using HaloDot.SdkUi;

namespace HaloDot.SdkUi.Example;

public static class MauiProgram
{
    public static MauiApp CreateMauiApp()
    {
        var builder = MauiApp.CreateBuilder()
            .UseMauiApp<App>()
            .UseHaloSdkUi();
        builder.Services.AddTransient<MainPage>();
        return builder.Build();
    }
}
