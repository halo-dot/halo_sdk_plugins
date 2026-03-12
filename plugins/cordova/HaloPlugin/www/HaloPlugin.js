var exec = require('cordova/exec');

// Empty constructor
function HaloPlugin() {}

HaloPlugin.prototype.registerCallbacks = async function(
  onRequestJWT,
  onHaloUIMessage,
  onInitializationResult,
  onHaloTransactionResult,
  onAttestationError,
  onSecurityError
) {
  cordova.exec(
    async (result) => {
      const returnObj = result?.result ? JSON.parse(result?.result) : null
      switch(result?.action) {
        case "onRequestJWT": {
          await onRequestJWT((jwt) => {
            console.log("giving JWT back to app")
            cordova.exec(null, null, 'HaloPlugin', 'onRequestJWTCallback', [jwt]);            
          })
        };
        break;

        case "onHaloUIMessage": {
          onHaloUIMessage(returnObj)
        };
        break;

        case "onInitializationResult": {
          onInitializationResult(returnObj)
        }
        break;

        case "onHaloTransactionResult": {
          onHaloTransactionResult(returnObj)
        }
        break;

        case "onAttestationError": {
          onAttestationError(returnObj)
        }
        break;

        case "onSecurityError": {
          onSecurityError(returnObj)
        }
        break;
      }
      console.log(`Callback called for ${result?.action}`);
    },
    async (errorResult) => {
      console.log(`Error result: ${JSON.stringify(errorResult)}`);
    },
    "HaloPlugin",
    "registerCallbacks"
  )
  console.log("Done registering!!!!!!!");
}

HaloPlugin.prototype.initialize = async function(successCallback, errorCallback, params) {
  cordova.exec(successCallback, errorCallback, 'HaloPlugin', 'initialize', [params]);
}

HaloPlugin.prototype.startTransaction = function(successCallback, errorCallback, params) {
  cordova.exec(successCallback, errorCallback, 'HaloPlugin', 'startTransaction', [params]);
}

HaloPlugin.prototype.requestTransactionCancellation = function(successCallback, errorCallback, params) {
  var options = params ? params : {};
  cordova.exec(successCallback, errorCallback, 'HaloPlugin', 'requestTransactionCancellation', [options]);
}

// Installation constructor that binds HaloPlugin to window
HaloPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.haloPlugin = new HaloPlugin();
  return window.plugins.haloPlugin;
};
cordova.addConstructor(HaloPlugin.install);