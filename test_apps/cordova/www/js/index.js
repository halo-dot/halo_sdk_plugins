document.addEventListener('deviceready', onDeviceReady, false);

async function onDeviceReady() {
    document.getElementById("btnInitialize").addEventListener("click", initializeButtonPressed);
    document.getElementById("btnTransact").addEventListener("click", transactButtonPressed);
    // document.getElementById("btnClearUI").addEventListener("click", clearUIMessages);
    document.getElementById("btnCancel").addEventListener("click", cancelTransactionPressed);
    await window.plugins.haloPlugin.registerCallbacks(
        onRequestJWT,
        onHaloUIMessage,
        onInitializationResult,
        onHaloTransactionResult,
        onAttestationError,
        onSecurityError
    );
    console.log("Finished awaiting")
}

async function onRequestJWT(callback) {
    console.log("getting jwt")
    const jwt = await getJWT();
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

function getJWT() {
    return new Promise((resolve, reject) => {
        cordova.plugin.http.post(
            'https://authserver.go.dev.haloplus.io/login',
            {
                "username" : "fredtma@gmail.com",
                "password" : "1111"
            },
            {},
            function(response) {
                try {
                    var data = JSON.parse(response.data);
                    jwt = data.token;
                    resolve(jwt);
                } catch(e) {
                    alert('JSON parsing error');
                }
            },
            function(response) {
                // prints 403
                console.log(response.status);
    
                //prints Permission denied
                alert(String(response.error));
            }
        );
    });
}

function initializeButtonPressed(){
    // var jwt = await getJWT();
    var cardTapTimeout = "20000", applicationName = "com.app.hf", applicationVersion = "1.0.0";
    // var jwt = "";
    // if(jwt){
        window.plugins.haloPlugin.initialize(
            async function(result) {
                // changeUI("initialisationResult", result);
                if(result === "requestingNewJWT"){
                    // console.log("Index.js initialiseButtonPressed: ", Date.now + " " + jwt);
                    // await refreshJWT("initialise");
                } /* else if(result.resultType === "Token refreshed successfully!"){
                    initializeButtonPressed();
                } */ else if(result.code){
                    alert(String(result.code));
                } else{
                    alert(String(result.resultType));
                }
            },
            function(result) {
                alert(String(result.resultType));
                // alert('Nothing to echo');
            },
            {
                cardTapTimeout,
                applicationName,
                applicationVersion
            }
        );
    // } else{
    //     // changeUI("initialisationResult", "Error fetching JWT");
    //     alert("Error fetching JWT");
    // }
}

function transactButtonPressed(){
    var transactionValue = document.getElementById("amountField").value;
    var merchantReference = document.getElementById("merchantReferenceField").value;
    var currency = document.getElementById("currencyDropdown").value;
    console.log(`This is the selected currency value: ${currency}`);
    if(transactionValue == null || transactionValue == "" || transactionValue.length == 0){
        alert('Please enter a valid value within the transaction amount field!');
    }
    else if(merchantReference == null || merchantReference == "" || merchantReference.length == 0){
        alert('Please enter a Merchant Reference!');
    }
    else if(currency == null || currency == "" || currency.length == 0){
        alert('Please select a currency!');
    }
    else{
        window.plugins.haloPlugin.startTransaction(
            async function(result) {
                if (result === "requestingNewJWT"){
                    alert("Fetching new JWT token");
                }
                else if(result.resultType){
                    alert(String(result.resultType));
                    console.log('Transaction Result: ', result);
                } 
                else if(result.code){
                    alert(String(result.code));
                }
                else {
                    alert(String(result.msgID));
                }
            },
            function(result) {
                alert(String(result.resultType));
            },
            {
                transactionValue,
                merchantReference,
                currency
            }
        );
    }
}

function cancelTransactionPressed(){
    window.plugins.haloPlugin.requestTransactionCancellation(
        function(result) {
            if(result.resultType){
                alert(String(result.resultType));
            } else {
                alert(String(result.msgID));
            }
        },
        function(result) {
            alert(String(result.resultType));
        }
    );
}