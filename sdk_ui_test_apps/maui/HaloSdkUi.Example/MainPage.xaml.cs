using System.Diagnostics;

namespace HaloDot.SdkUi.Example;

/// <summary>Every call the package offers, on one screen.</summary>
public partial class MainPage : ContentPage
{
    private static readonly Color Success = Color.FromArgb("#1E9E5A");
    private static readonly Color Error = Color.FromArgb("#D93A3A");
    private static readonly Color Warning = Color.FromArgb("#D98A1A");

    private readonly IHaloSdkUi _haloSdkUi;
    private readonly Dictionary<Grid, (VerticalStackLayout Section, Label Chevron)> _sections;

    private string _sdkVersion = "Unknown";
    private bool? _nfc;
    private string _installationId = "Not registered";
    private string? _lastOutcome;
    private string _printerStatus = "Not checked";
    private bool _hasPrinter;
    private bool _nfcListening;
    private bool _afrikaans;
    private bool _started;

    public MainPage(IHaloSdkUi haloSdkUi)
    {
        InitializeComponent();
        _haloSdkUi = haloSdkUi;
        _sections = new()
        {
            [PaymentsHeader] = (PaymentsSection, PaymentsChevron),
            [DeviceHeader] = (DeviceSection, DeviceChevron),
            [PrinterHeader] = (PrinterSection, PrinterChevron),
            [SamplesHeader] = (SamplesSection, SamplesChevron),
        };
        foreach (var (section, chevron) in _sections.Values) ShowChevron(section, chevron);
        ShowStatus();
    }

    protected override async void OnAppearing()
    {
        base.OnAppearing();
        if (_started) return;
        _started = true;
        await StartAsync();
    }

    private async Task StartAsync()
    {
        await _haloSdkUi.PrepareAsync();

        _sdkVersion = await _haloSdkUi.SdkInfoAsync() ?? "Unknown";
        _nfc = await _haloSdkUi.NfcAsync();
        _installationId = await _haloSdkUi.DeviceInstallationIdAsync() ?? "Not registered";

        ShowStatus();

        // Set before init, because registering the device needs a JWT.
        _haloSdkUi.OnTokenRequest(JwtToken.CreateAsync);

        var init = await _haloSdkUi.InitAsync();

        Debug.WriteLine($"init -> {init}");
        Report(init.IsInitialized
            ? "SDK ready"
            : $"SDK not ready: {init.ErrorTitle ?? init.ErrorCode}. {init.ErrorMessage}");
        _installationId = await _haloSdkUi.DeviceInstallationIdAsync() ?? "Not registered";

        await CheckPrinterAsync();
    }

    private void ShowStatus()
    {
        SdkValue.Text = _sdkVersion;
        InstallationValue.Text = _installationId;
        PrinterValue.Text = _printerStatus;

        NfcValue.Text = _nfc switch
        {
            null => "No hardware",
            true => "On",
            false => "Off"
        };
        NfcValue.TextColor = _nfc switch
        {
            null => Error,
            true => Success,
            false => Warning
        };

        // No payment yet keeps the default colour.
        OutcomeValue.Text = _lastOutcome ?? "—";
        if (_lastOutcome is not null)
        {
            var paymentApproved = _lastOutcome == "Approved";
            OutcomeValue.TextColor = paymentApproved ? Success : Error;
        }
    }

    private void Report(string action)
    {
        ActionLabel.Text = action;
        ShowStatus();
    }

    private static void ShowChevron(VerticalStackLayout section, Label chevron) =>
        chevron.Text = section.IsVisible ? "▲" : "▼";

    private void OnToggleSection(object? sender, TappedEventArgs e)
    {
        var (section, chevron) = _sections[(Grid)sender!];
        section.IsVisible = !section.IsVisible;
        ShowChevron(section, chevron);
    }

    // ─── Payments ─────────────────────────────────────────────────────────

    private async Task ChargeAsync(string label, decimal? amount, Presentation? presentation = null)
    {
        Report($"{label}…");

        var result = await _haloSdkUi.TransactAsync(
            amount,
            "ZAR",
            reference: amount is null ? null : $"example-{DateTimeOffset.UtcNow.ToUnixTimeMilliseconds()}",
            presentation: presentation);

        _lastOutcome = result.ResultType ?? "No result";
        Debug.WriteLine($"transact -> {result}");

        Report(result.ResultType is null
            ? $"{label}: left before a card was read"
            : $"{label}: {result.ResultType}{(result.MaskedPan is null ? "" : $", card {result.MaskedPan}")}");
    }

    private async void OnCharge(object? sender, EventArgs e) => await ChargeAsync("Charge R10.00", 10.00m);

    private async void OnChargeAsSheet(object? sender, EventArgs e) =>
        await ChargeAsync("Charge R10.00 as a sheet", 10.00m, Presentation.Sheet);

    private async void OnChargeOnKeypad(object? sender, EventArgs e) => await ChargeAsync("Charge on the keypad", null);

    // ─── Device and settings ──────────────────────────────────────────────

    private async void OnOpenNfcSettings(object? sender, EventArgs e) =>
        Report(await _haloSdkUi.OpenNfcSettingsAsync() ? "NFC settings opened" : "NFC settings: nothing would open");

    // Only the SDK's payment screens change language, from the next payment; this app's own text stays as is.
    private async void OnToggleLanguage(object? sender, EventArgs e)
    {
        _afrikaans = !_afrikaans;
        await _haloSdkUi.LanguageAsync(_afrikaans ? "af" : "en");
        LanguageButton.Text = _afrikaans ? "Switch to English" : "Switch to Afrikaans";
        Report($"Payment screens now in {(_afrikaans ? "Afrikaans" : "English")}: see the next charge");
    }

    private async void OnOpenDocs(object? sender, EventArgs e) =>
        Report(await _haloSdkUi.BrowserAsync("https://docs.halodot.io") ? "Docs opened in the browser" : "No browser would open the docs");

    private async void OnForgetRegistration(object? sender, EventArgs e)
    {
        await _haloSdkUi.ForgetRegistrationAsync();
        _installationId = await _haloSdkUi.DeviceInstallationIdAsync() ?? "Not registered";
        Report("Terminal registration forgotten: it registers again on the next init");
    }

    // ─── Printer ──────────────────────────────────────────────────────────

    private async Task CheckPrinterAsync()
    {
        try
        {
            var printer = await _haloSdkUi.GetPrinterAsync();
            _hasPrinter = printer is not null;
            _printerStatus = printer is null ? "None found" : $"Ready ({printer})";
        }
        catch (HaloPrinterException ex)
        {
            _hasPrinter = false;
            _printerStatus = $"{ex.Code}: {ex.Message}";
        }
        ShowStatus();
    }

    private async Task PrintAsync(string label, Printable printable)
    {
        if (!_hasPrinter)
        {
            Report($"Can't print {label}: no printer. Check or scan for one first.");
            return;
        }
        try
        {
            Report($"Printing {label}…");
            await _haloSdkUi.PrintAsync(printable);
            Report($"Printed {label}");
        }
        catch (HaloPrinterException ex)
        {
            Report($"Print failed: {ex.Code}: {ex.Message}");
        }
    }

    private async void OnCheckPrinter(object? sender, EventArgs e)
    {
        Report("Checking for a printer…");
        await CheckPrinterAsync();
        Report(_hasPrinter ? $"Printer {_printerStatus}" : "No printer on this device, and none selected");
    }

    private async void OnScanForPrinter(object? sender, EventArgs e)
    {
        Report("Scanning for Zebra printers…");
        try
        {
            var printers = await _haloSdkUi.ScanForPrintersAsync();
            if (printers.Count == 0)
            {
                Report("No Zebra printer found");
                return;
            }
            // The SDK selects a lone printer itself; with several, the example takes the first.
            if (printers.Count > 1) await _haloSdkUi.SelectPrinterAsync(printers[0]);
            await CheckPrinterAsync();
            Report($"Found {printers.Count}; selected {printers[0].Name}");
        }
        catch (HaloPrinterException ex)
        {
            Report($"Scan failed: {ex.Code}: {ex.Message}");
        }
    }

    private async void OnToggleNfcTaps(object? sender, EventArgs e)
    {
        if (_nfcListening)
        {
            await _haloSdkUi.DisablePrinterTapsAsync();
            _nfcListening = false;
            NfcTapsButton.Text = "Listen for printer taps";
            Report("Stopped listening for printer taps");
            return;
        }

        var canTap = await _haloSdkUi.EnablePrinterTapsAsync(async device =>
        {
            await CheckPrinterAsync();
            Report(device is null ? "Tapped a tag that named no printer" : $"Tapped {device.Name}; selected it");
        });
        if (!canTap)
        {
            Report("Can't listen for taps: this device has no NFC");
            return;
        }
        _nfcListening = true;
        NfcTapsButton.Text = "Stop listening for printer taps";
        Report("Listening: tap a Zebra Print Touch tag");
    }

    private async void OnPrintReceipt(object? sender, EventArgs e) =>
        await PrintAsync("a receipt", Printable.Receipt(new Receipt(
            new TextLine("Halo Dot", Size: 34, Bold: true, Align: PrintAlign.Center),
            new FeedLine(),
            new TextLine("Approved", Align: PrintAlign.Center),
            new DividerLine(),
            new TextLine("Amount   R10.00"),
            new TextLine("Card     **** 1234"),
            new TextLine("Auth     A1B2C3"),
            new DividerLine(),
            new QrLine("example-receipt", Size: 240),
            new FeedLine(2))));

    private async void OnPrintText(object? sender, EventArgs e) =>
        await PrintAsync("a line of text", Printable.Text("Hello from HaloDot.SdkUi.Maui"));

    // ─── Sample till slips ────────────────────────────────────────────────

    private async void OnPrintSample1(object? sender, EventArgs e) =>
        await PrintAsync("till slip 1", Printable.Text(SampleReceipts.Example1, clean: true));

    private async void OnPrintSample2(object? sender, EventArgs e) =>
        await PrintAsync("till slip 2", Printable.Text(SampleReceipts.Example2, clean: true));

    private async void OnPrintSample1Centred(object? sender, EventArgs e) =>
        await PrintAsync("till slip 1, centred", Printable.Text(SampleReceipts.Example1, clean: true, center: true));

    private async void OnPrintSample2Centred(object? sender, EventArgs e) =>
        await PrintAsync("till slip 2, centred", Printable.Text(SampleReceipts.Example2, clean: true, center: true));
}
