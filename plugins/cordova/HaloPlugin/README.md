# HaloPlugin

# Please note this plugin overwrites the Android native MainActivity.js file

# Steps to install and use HaloPlugin:

1. Install node package 'xml2js' in your core cordova project using: npm i xml2js (The plugin needs this package to work correctly).
    If you add the plugin without this package: Remove the plugin, add the package, then re-add the plugin.
2. Provide the following keys in your core project gradle local.properties file (these will be provided to you by synthesis):
    aws.accesskey=
    aws.secretkey=
3. The SDK requires a minSdkVersion of 26, you can specify this in your config.xml file as such:
    <preference name="android-minSdkVersion" value="26" />
   You can also set the targetSdkVersion required of your cordova project as such: 
    <preference name="android-targetSdkVersion" value="31" />
4. Install HaloPlugin using: 'cordova plugin add {path to HaloPlugin}' (e.g. 'cordova plugin add ../HaloPlugin/' assuming they are in the same root folder).
5. Perform a gradle sync in Android Studio
6. You should now have access to the 'za.co.synthesis.halo.sdk' namespace
7. Be sure that your application has camera, microphone and location permissions before using the plugin.
8. To ensure that the Pin Screen works, add the following tag to the Application Layer/Tag of your AndroidManifest.xml file:
```android:theme="@style/Theme.AppCompat.Light.NoActionBar.FullScreen"```

# To reference the plugin from your own main project:
The plugin has four main functions, namely:
    'registerCallbacks', 'initialize', 'startTransaction', and 'requestTransactionCancellation'

These can be referenced as such:

## registerCallbacks
async function onDeviceReady() {
    await window.plugins.haloPlugin.registerCallbacks(
        onRequestJWT,
        onHaloUIMessage,
        onInitializationResult,
        onHaloTransactionResult,
        onAttestationError,
        onSecurityError
    );
}

The register callbacks is for all callbacks sent by the SDK.

These can be implemented as such:

```
async function onRequestJWT(callback) {
    console.log("getting jwt")
    const jwt = await getJWT();     //function to get new JWT to send through to the SDK
    console.log("got jwt")
    callback(jwt)
}

function onHaloUIMessage(haloUIMessage) {
    alert(JSON.stringify(haloUIMessage, null, 2))
}

function onInitializationResult(haloInitialisationResult) {
    console.log(`onInitialisationResult:`)
    alert(`${JSON.stringify(haloInitialisationResult)}`)
}

function onHaloTransactionResult(haloTransactionResult) {
    console.log(`onHaloTransactionResult:`)
    console.log(`${JSON.stringify(haloTransactionResult)}`)
}

function onAttestationError(haloAttestationHealthResult) {
    console.log(`onAttestationError:`)
    console.log(`${JSON.stringify(haloAttestationHealthResult)}`)
}

function onSecurityError(haloErrorCode) {
    console.log(`onSecurityError:`)
    console.log(`${JSON.stringify(haloErrorCode)}`)
}
```

## initialize
window.plugins.haloPlugin.initialize(
    function(result) {},    //successcallback
    function() {},          //errorcallback
    {
        cardTapTimeout,     //
        applicationName,    // initialisation parameters required as an object (each parameter itself in this object must be of type string)
        applicationVersion  //
    }
);

## startTransaction
window.plugins.haloPlugin.startTransaction(
    function(result) {},    //successcallback
    function() {},          //errorcallback
    {
        transactionValue,   //transactionvalue (transaction amount in rands) to be passed in as a parameter
        merchantReference   //merchantReference to be passed in as a parameter
        currency            //currency (zar, nad, usd) to be passed in as a parameter
    }
);

## requestTransactionCancellation
window.plugins.haloPlugin.requestTransactionCancellation(
    function(result) {},    //successcallback
    function(result) {}     //errorcallback
);

# Please note that all callbacks will now be sent through the functions in the registerCallbacks method.