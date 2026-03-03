"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.HaloSdk = void 0;
const react_native_1 = require("react-native");
const LINKING_ERROR = `The package 'halo-sdk-react-native' doesn't seem to be linked. Make sure to: \n\n` +
    `1. Add HaloSdkPackage to your MainApplication's getPackages() list.\n` +
    `2. Extend HaloReactActivity instead of ReactActivity in MainActivity.\n` +
    `3. Copy the Halo AAR libs to your android/libs folder.`;
const HaloSdkNative = react_native_1.NativeModules.HaloSdk
    ? react_native_1.NativeModules.HaloSdk
    : new Proxy({}, {
        get() {
            throw new Error(LINKING_ERROR);
        },
    });
const eventEmitter = new react_native_1.NativeEventEmitter(HaloSdkNative);
const HALO_SDK_EVENT = 'haloSdkEvent';
let _subscription = null;
function _setupEventListener(callbacks) {
    _subscription === null || _subscription === void 0 ? void 0 : _subscription.remove();
    _subscription = eventEmitter.addListener(HALO_SDK_EVENT, (event) => {
        switch (event.eventType) {
            case 'attestation':
                callbacks.onAttestationError(event.data);
                break;
            case 'transaction':
                callbacks.onHaloTransactionResult(event.data);
                break;
            case 'ui':
                callbacks.onHaloUIMessage(event.data);
                break;
            case 'initialization':
                callbacks.onInitializationResult(event.data);
                break;
            case 'onJwtRequest':
                callbacks.onRequestJWT(_jwtCallback);
                break;
            case 'security':
                callbacks.onSecurityError(event.data);
                break;
            case 'camera':
                callbacks.onCameraControlLost();
                break;
        }
    });
}
function _jwtCallback(jwt) {
    HaloSdkNative.jwtCallback(jwt);
}
exports.HaloSdk = {
    /**
     * Initialize the Halo SDK. Must be called before any transactions.
     *
     * @param callbacks - Object implementing IHaloCallbacks to receive SDK events.
     * @param applicationPackageName - Your app's package name.
     * @param applicationVersion - Your app's version string.
     * @param onStartTransactionTimeOut - Timeout in ms for starting a transaction (default 300000).
     * @param enableSchemeAnimations - Whether to show Visa/Mastercard/Amex animations on approval.
     */
    initialize(callbacks, applicationPackageName, applicationVersion, onStartTransactionTimeOut, enableSchemeAnimations) {
        _setupEventListener(callbacks);
        return HaloSdkNative.initializeHaloSDK({
            applicationPackageName,
            applicationVersion,
            onStartTransactionTimeOut: onStartTransactionTimeOut !== null && onStartTransactionTimeOut !== void 0 ? onStartTransactionTimeOut : null,
            enableSchemeAnimations: enableSchemeAnimations !== null && enableSchemeAnimations !== void 0 ? enableSchemeAnimations : false,
        });
    },
    /**
     * Start a purchase transaction.
     *
     * @param transactionAmount - Amount to charge (e.g. 10.50).
     * @param merchantTransactionReference - Unique reference for this transaction.
     * @param transactionCurrency - ISO 4217 currency code (e.g. "ZAR").
     * @returns HaloStartTransactionResult indicating whether the tap was accepted.
     */
    startTransaction(transactionAmount, merchantTransactionReference, transactionCurrency) {
        return HaloSdkNative.startTransaction({
            transactionAmount,
            merchantTransactionReference,
            transactionCurrency,
        });
    },
    /**
     * Start a card refund transaction.
     *
     * @param transactionAmount - Amount to refund.
     * @param merchantTransactionReference - Unique reference for this transaction.
     * @param transactionCurrency - ISO 4217 currency code (e.g. "ZAR").
     * @returns HaloStartTransactionResult indicating whether the tap was accepted.
     */
    cardRefundTransaction(transactionAmount, merchantTransactionReference, transactionCurrency) {
        return HaloSdkNative.cardRefundTransaction({
            transactionAmount,
            merchantTransactionReference,
            transactionCurrency,
        });
    },
    /**
     * Request cancellation of the current transaction.
     */
    cancelTransaction() {
        return HaloSdkNative.cancelTransaction();
    },
};
