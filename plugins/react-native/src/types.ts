export interface HaloStartTransactionResult {
  resultType: string;
  errorCode: string;
}

export interface HaloAttestationHealthResult {
  resultType: string;
  errorCode: string;
}

export interface HaloCurrencyInfo {
  currencyCode: string;
  defaultFractionDigits: number;
  numericCode: number;
  displayName: string;
}

export interface HaloCurrencyValue {
  currency?: HaloCurrencyInfo;
  amount?: number;
}

export interface HaloWarning {
  errorCode: string;
  details?: string;
}

export interface HaloInitializationResult {
  resultType: string;
  terminalCurrency?: HaloCurrencyInfo;
  terminalLanguageCodes?: string[];
  terminalCountryCode?: string;
  errorCode: string;
  warnings: HaloWarning[];
}

export interface HaloTransactionReceipt {
  signature?: string;
  transactionDate?: string;
  transactionTime?: string;
  aid?: string;
  applicationLabel?: string;
  applicationPreferredName?: string;
  tvr?: string;
  cvr?: string;
  cryptogramType?: string;
  cryptogram?: string;
  maskedPAN?: string;
  authorizationCode?: string;
  ISOResponseCode?: string;
  association?: string;
  expiryDate?: string;
  mid?: string;
  merchantName?: string;
  tid?: string;
  stan?: string;
  panEntry?: string;
  cardType?: string;
  panSequenceNumber?: string;
  effectiveDate?: string;
  disposition?: string;
  currencyCode?: string;
  amountAuthorised?: string;
  amountOther?: string;
}

export interface HaloTransactionResult {
  resultType: string;
  merchantTransactionReference?: string;
  haloTransactionReference?: string;
  paymentProviderReference?: string;
  errorCode: string;
  errorDetails?: string;
  receipt?: HaloTransactionReceipt;
  customTags?: Record<string, string>;
}

export interface HaloUIMessage {
  msgID: string;
  holdTimeMS?: number;
  languagePreference?: string;
  offlineBalance?: HaloCurrencyValue;
  transactionAmount?: HaloCurrencyValue;
}

export interface IHaloCallbacks {
  onAttestationError: (details: HaloAttestationHealthResult) => void;
  onHaloTransactionResult: (result: HaloTransactionResult) => void;
  onHaloUIMessage: (message: HaloUIMessage) => void;
  onInitializationResult: (result: HaloInitializationResult) => void;
  onRequestJWT: (jwtCallback: (jwt: string) => void) => void;
  onSecurityError: (errorCode: string) => void;
  onCameraControlLost: () => void;
}
