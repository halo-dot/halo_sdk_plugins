import type { IHaloCallbacks, HaloStartTransactionResult } from './types';
export declare const HaloSdk: {
    /**
     * Initialize the Halo SDK. Must be called before any transactions.
     *
     * @param callbacks - Object implementing IHaloCallbacks to receive SDK events.
     * @param applicationPackageName - Your app's package name.
     * @param applicationVersion - Your app's version string.
     * @param onStartTransactionTimeOut - Timeout in ms for starting a transaction (default 300000).
     * @param enableSchemeAnimations - Whether to show Visa/Mastercard/Amex animations on approval.
     */
    initialize(callbacks: IHaloCallbacks, applicationPackageName: string, applicationVersion: string, onStartTransactionTimeOut?: number, enableSchemeAnimations?: boolean): Promise<void>;
    /**
     * Start a purchase transaction.
     *
     * @param transactionAmount - Amount to charge (e.g. 10.50).
     * @param merchantTransactionReference - Unique reference for this transaction.
     * @param transactionCurrency - ISO 4217 currency code (e.g. "ZAR").
     * @returns HaloStartTransactionResult indicating whether the tap was accepted.
     */
    startTransaction(transactionAmount: number, merchantTransactionReference: string, transactionCurrency: string): Promise<HaloStartTransactionResult>;
    /**
     * Start a card refund transaction.
     *
     * @param transactionAmount - Amount to refund.
     * @param merchantTransactionReference - Unique reference for this transaction.
     * @param transactionCurrency - ISO 4217 currency code (e.g. "ZAR").
     * @returns HaloStartTransactionResult indicating whether the tap was accepted.
     */
    cardRefundTransaction(transactionAmount: number, merchantTransactionReference: string, transactionCurrency: string): Promise<HaloStartTransactionResult>;
    /**
     * Request cancellation of the current transaction.
     */
    cancelTransaction(): Promise<void>;
};
//# sourceMappingURL=HaloSdk.d.ts.map