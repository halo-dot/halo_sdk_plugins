package za.co.synthesis.halo.sdkreactnativeplugin

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
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

internal fun makeWritableMap(data: HaloAttestationHealthResult): WritableMap {
    return Arguments.createMap().apply {
        putString("resultType", data.resultType.name)
        putString("errorCode", data.errorCode.name)
    }
}

internal fun makeWritableMap(data: HaloTransactionResult): WritableMap {
    return Arguments.createMap().apply {
        putString("resultType", data.resultType.name)
        putString("merchantTransactionReference", data.merchantTransactionReference)
        putString("haloTransactionReference", data.haloTransactionReference)
        putString("paymentProviderReference", data.paymentProviderReference)
        putString("errorCode", data.errorCode.name)
        putString("errorDetails", data.errorDetails)
        putMap("receipt", makeWritableMap(data.receipt))
        putMap("customTags", data.customTags?.toWritableMap())
    }
}

internal fun makeWritableMap(data: HaloTransactionReceipt?): WritableMap? {
    if (data == null) return null
    return Arguments.createMap().apply {
        putString("signature", data.signature)
        putString("transactionDate", data.transactionDate)
        putString("transactionTime", data.transactionTime)
        putString("aid", data.aid)
        putString("applicationLabel", data.applicationLabel)
        putString("applicationPreferredName", data.applicationPreferredName)
        putString("tvr", data.tvr)
        putString("cvr", data.cvr)
        putString("cryptogramType", data.cryptogramType?.name)
        putString("cryptogram", data.cryptogram)
        putString("maskedPAN", data.maskedPAN)
        putString("authorizationCode", data.authorizationCode)
        putString("ISOResponseCode", data.ISOResponseCode)
        putString("association", data.association)
        putString("expiryDate", data.expiryDate)
        putString("mid", data.mid)
        putString("merchantName", data.merchantName)
        putString("tid", data.tid)
        putString("stan", data.stan)
        putString("panEntry", data.panEntry)
        putString("cardType", data.cardType)
        putString("panSequenceNumber", data.panSequenceNumber)
        putString("effectiveDate", data.effectiveDate)
        putString("disposition", data.disposition)
        putString("currencyCode", data.currencyCode)
        putString("amountAuthorised", data.amountAuthorised)
        putString("amountOther", data.amountOther)
    }
}

internal fun makeWritableMap(data: HaloUIMessage): WritableMap {
    return Arguments.createMap().apply {
        putString("msgID", data.msgID.name)
        putInt("holdTimeMS", data.holdTimeMS)
        putString("languagePreference", data.languagePreference)
        putMap("offlineBalance", makeWritableMap(data.offlineBalance))
        putMap("transactionAmount", makeWritableMap(data.transactionAmount))
    }
}

internal fun makeWritableMap(data: HaloCurrencyValue?): WritableMap? {
    if (data == null) return null
    return Arguments.createMap().apply {
        putMap("currency", makeWritableMap(data.currency))
        putDouble("amount", data.amount.toDouble())
    }
}

internal fun makeWritableMap(data: Currency?): WritableMap? {
    if (data == null) return null
    return Arguments.createMap().apply {
        putString("currencyCode", data.currencyCode)
        putInt("defaultFractionDigits", data.defaultFractionDigits)
        putInt("numericCode", data.numericCode)
        putString("displayName", data.displayName)
    }
}

internal fun makeWritableMap(data: HaloInitializationResult): WritableMap {
    return Arguments.createMap().apply {
        putString("resultType", data.resultType.name)
        putMap("terminalCurrency", makeWritableMap(data.terminalCurrency))
        putArray("terminalLanguageCodes", data.terminalLanguageCodes?.toWritableArray())
        putString("terminalCountryCode", data.terminalCountryCode)
        putString("errorCode", data.errorCode.name)
        putArray("warnings", data.warnings.toWritableArray { makeWritableMap(it) })
    }
}

internal fun makeWritableMap(data: HaloWarning): WritableMap {
    return Arguments.createMap().apply {
        putString("errorCode", data.errorCode.name)
        putString("details", data.details)
    }
}

internal fun makeWritableMap(data: HaloStartTransactionResult): WritableMap {
    return Arguments.createMap().apply {
        putString("resultType", data.resultType.name)
        putString("errorCode", data.errorCode.name)
    }
}

private fun Map<String, String>.toWritableMap(): WritableMap {
    return Arguments.createMap().apply {
        this@toWritableMap.forEach { (key, value) -> putString(key, value) }
    }
}

private fun List<String>.toWritableArray(): WritableArray {
    return Arguments.createArray().apply {
        this@toWritableArray.forEach { pushString(it) }
    }
}

private fun <T> List<T>.toWritableArray(transform: (T) -> WritableMap): WritableArray {
    return Arguments.createArray().apply {
        this@toWritableArray.forEach { pushMap(transform(it)) }
    }
}
