namespace HaloDot.SdkUi.Example;

public class App(IServiceProvider services) : Application
{
    protected override Window CreateWindow(IActivationState? activationState) =>
        new(services.GetRequiredService<MainPage>());
}
