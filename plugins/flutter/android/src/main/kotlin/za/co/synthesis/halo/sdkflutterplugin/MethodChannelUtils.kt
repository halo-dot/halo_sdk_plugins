package za.co.synthesis.halo.sdkflutterplugin

import za.co.synthesis.halo.haloCommonInterface.HaloTransactionReceipt
import za.co.synthesis.halo.haloCommonInterface.HaloTransactionResult
import za.co.synthesis.halo.sdk.model.HaloAttestationHealthResult
import za.co.synthesis.halo.sdk.model.HaloCurrencyValue
import za.co.synthesis.halo.sdk.model.HaloInitializationResult
import za.co.synthesis.halo.sdk.model.HaloStartTransactionResult
import za.co.synthesis.halo.sdk.model.HaloUIMessage
import za.co.synthesis.halo.sdk.model.HaloWarning
import java.util.Currency

internal fun <T> getRest(args: Map<String, Any>, keys: List<String>): Map<String, T> {
    val rest = HashMap<String, T>()

    for (key in args.keys) {
        try {
            if (key !in keys) {
                rest[key] = args[key] as T
            }
        } catch (e: TypeCastException) {
            throw TypeCastException("The value of $key is not of type T")
        }
    }

    return rest
}

internal fun makeMap(data: HaloAttestationHealthResult): HashMap<String, Any> {
    return hashMapOf(
        Pair("resultType", data.resultType.name),
        Pair("errorCode", data.errorCode.name)
    )
}

internal fun makeMap(data: HaloTransactionResult): HashMap<String, Any?> {
    return hashMapOf(
        Pair("resultType", data.resultType.name),
        Pair("merchantTransactionReference", data.merchantTransactionReference),
        Pair("haloTransactionReference", data.haloTransactionReference),
        Pair("paymentProviderReference", data.paymentProviderReference),
        Pair("errorCode", data.errorCode.name),
        Pair("errorDetails", data.errorDetails),
        Pair("receipt", makeMap(data.receipt)),
        Pair("customTags", data.customTags)
    )
}

internal fun makeMap(data: HaloTransactionReceipt?): HashMap<String, Any?>? {
    return if (data == null) {
        null
    } else {
        hashMapOf(
            Pair("signature", data.signature),
            Pair("transactionDate", data.transactionDate),
            Pair("transactionTime", data.transactionTime),
            Pair("aid", data.aid),
            Pair("applicationLabel", data.applicationLabel),
            Pair("applicationPreferredName", data.applicationPreferredName),
            Pair("tvr", data.tvr),
            Pair("cvr", data.cvr),
            Pair("cryptogramType", data.cryptogramType?.name),
            Pair("cryptogram", data.cryptogram),
            Pair("maskedPAN", data.maskedPAN),
            Pair("authorizationCode", data.authorizationCode),
            Pair("ISOResponseCode", data.ISOResponseCode),
            Pair("association", data.association),
            Pair("expiryDate", data.expiryDate),
            Pair("mid", data.mid),
            Pair("merchantName", data.merchantName),
            Pair("tid", data.tid),
            Pair("stan", data.stan),
            Pair("panEntry", data.panEntry),
            Pair("cardType", data.cardType),
            Pair("panSequenceNumber", data.panSequenceNumber),
            Pair("effectiveDate", data.effectiveDate),
            Pair("disposition", data.disposition),
            Pair("currencyCode", data.currencyCode),
            Pair("amountAuthorised", data.amountAuthorised),
            Pair("amountOther", data.amountOther)
        )
    }
}

internal fun makeMap(data: HaloUIMessage): HashMap<String, Any?> {
    return hashMapOf(
        Pair("msgID", data.msgID.name),
        Pair("holdTimeMS", data.holdTimeMS),
        Pair("languagePreference", data.languagePreference),
        Pair("offlineBalance", makeMap(data.offlineBalance)),
        Pair("transactionAmount", makeMap(data.transactionAmount))
    )
}

internal fun makeMap(data: HaloCurrencyValue?): HashMap<String, Any>? {
    if (data == null) {
        return null
    }
    return hashMapOf(
        Pair("currency", makeMap(data.currency)),
        Pair("amount", data.amount.toDouble())
    )
}

internal fun makeMap(data: Currency): HashMap<String, Any> {
    return hashMapOf(
        Pair("currencyCode", data.currencyCode),
        Pair("defaultFractionDigits", data.defaultFractionDigits),
        Pair("numericCode", data.numericCode),
        Pair("displayName", data.displayName)
    )
}

internal fun makeMap(data: HaloInitializationResult): HashMap<String, Any> {
    return hashMapOf(
        Pair("resultType", data.resultType.name),
        Pair("terminalCurrency", makeMap(data.terminalCurrency)),
        Pair("terminalLanguageCodes", data.terminalLanguageCodes),
        Pair("terminalCountryCode", data.terminalCountryCode),
        Pair("errorCode", data.errorCode.name),
        Pair("warnings", data.warnings.map { makeMap(it) })
    )
}

internal fun makeMap(data: HaloWarning): HashMap<String, Any> {
    return hashMapOf(
        Pair("errorCode", data.errorCode.name),
        Pair("details", data.details)
    )
}

internal fun makeMap(data: HaloStartTransactionResult): HashMap<String, Any> {
    return hashMapOf(
        Pair("resultType", data.resultType.name),
        Pair("errorCode", data.errorCode.name)
    )
}
