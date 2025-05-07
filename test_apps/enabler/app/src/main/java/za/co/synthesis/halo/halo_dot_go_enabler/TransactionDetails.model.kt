package za.co.synthesis.halo.halo_dot_go_enabler

class TransactionDetails{
    var id: String
    var passthroughUserId: String
    var merchantTransactionReference: String
    var latitude: String
    var longitude: String
    var deviceinstallationId: String
    var transactionstatusId: Int
    var transactionDispositionId: Int
    var amount: String
    var currency: String
    var responseCode: Int
    var transactionTypeId: Int
    var originalTransactionId: String
    var acquirerId: Int
    var userId: String
    var transactionFeesConfigId: Int
    var transactionFee: String
    var authorisationCode: String
    var createdAt: String
    var updatedAt: String

    var details: MutableList<TransactionDetailsDetails>? = null
    var receiptData: ReceiptData?

    constructor(
        id: String,
        passthroughUserId: String,
        merchantTransactionReference: String,
        latitude: String,
        longitude: String,
        deviceinstallationId: String,
        transactionstatusId: Int,
        transactionDispositionId: Int,
        amount: String,
        currency: String,
        responseCode: Int,
        transactionTypeId: Int,
        originalTransactionId: String,
        acquirerId: Int,
        userId: String,
        transactionFeesConfigId: Int,
        transactionFee: String,
        authorisationCode: String,
        createdAt: String,
        updatedAt: String,
        details: MutableList<TransactionDetailsDetails>,
        receiptData: ReceiptData,
    ) {
        this.id = id
        this.passthroughUserId = passthroughUserId
        this.merchantTransactionReference = merchantTransactionReference
        this.latitude = latitude
        this.longitude = longitude
        this.deviceinstallationId = deviceinstallationId
        this.transactionstatusId = transactionstatusId
        this.transactionDispositionId = transactionDispositionId
        this.amount = amount
        this.currency = currency
        this.responseCode = responseCode
        this.transactionTypeId = transactionTypeId
        this.originalTransactionId = originalTransactionId
        this.acquirerId = acquirerId
        this.userId = userId
        this.transactionFeesConfigId = transactionFeesConfigId
        this.transactionFee = transactionFee
        this.authorisationCode = authorisationCode
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.details = details
        this.receiptData = receiptData
    }

    constructor() {
        this.id = ""
        this.passthroughUserId = ""
        this.merchantTransactionReference = ""
        this.latitude = ""
        this.longitude = ""
        this.deviceinstallationId = ""
        this.transactionstatusId = 0
        this.transactionDispositionId = 0
        this.amount = ""
        this.currency = ""
        this.responseCode = 0
        this.transactionTypeId = 0
        this.originalTransactionId = ""
        this.acquirerId = 0
        this.userId = ""
        this.transactionFeesConfigId = 0
        this.transactionFee = ""
        this.authorisationCode = ""
        this.createdAt = ""
        this.updatedAt = ""
        this.details = null
        this.receiptData = null
    }
}

class TransactionDetailsDetails {
    var maskedPAN: String
    var encryptedV2: TransactionEncryptedV2Details?
    var paymentProviderId: String

    constructor(
        maskedPAN: String,
        encryptedV2: TransactionEncryptedV2Details,
        paymentProviderId: String,
    ) {
        this.maskedPAN = maskedPAN
        this.encryptedV2 = encryptedV2
        this.paymentProviderId = paymentProviderId
    }

    constructor() {
        this.maskedPAN = ""
        this.encryptedV2 = null
        this.paymentProviderId = ""
    }
}

class TransactionEncryptedV2Details {
    var jwe: String
    var keyVersion: String
    var fingerprint: String

    constructor(
        jwe: String,
        keyVersion: String,
        fingerprint: String,
    ) {
        this.jwe = jwe
        this.keyVersion = keyVersion
        this.fingerprint = fingerprint
    }

    constructor() {
        this.jwe = ""
        this.keyVersion = ""
        this.fingerprint = ""
    }
}

class ReceiptData {
    var effectiveDate: String
    var expiryDate: String
    var tid: String
    var transactionReference: String
    var merchantName: String
    var formattedMerchantAddress: String
    var transactionType: String
    var approvalText: String
    var aid: String
    var applicationLabel: String
    var applicationPreferredName: String
    var association: String
    var isoResponseCode: String
    var cryptogram: String
    var cryptogramType: String
    var tvr: String
    var date: String
    var time: String
    var maskedPan: String


    constructor(
        effectiveDate: String,
        expiryDate: String,
        tid: String,
        transactionReference: String,
        merchantName: String,
        formattedMerchantAddress: String,
        transactionType: String,
        approvalText: String,
        aid: String,
        applicationLabel: String,
        applicationPreferredName: String,
        association: String,
        isoResponseCode: String,
        cryptogram: String,
        cryptogramType: String,
        tvr: String,
        date: String,
        time: String,
        maskedPan: String,
    ) {
        this.effectiveDate = effectiveDate
        this.expiryDate = expiryDate
        this.tid = tid
        this.transactionReference = transactionReference
        this.merchantName = merchantName
        this.formattedMerchantAddress = formattedMerchantAddress
        this.transactionType = transactionType
        this.approvalText = approvalText
        this.aid = aid
        this.applicationLabel = applicationLabel
        this.applicationPreferredName = applicationPreferredName
        this.association = association
        this.isoResponseCode = isoResponseCode
        this.cryptogram = cryptogram
        this.cryptogramType = cryptogramType
        this.tvr = tvr
        this.date = date
        this.time = time
        this.maskedPan = maskedPan
    }

    constructor() {
        this.effectiveDate = ""
        this.expiryDate = ""
        this.tid = ""
        this.transactionReference = ""
        this.merchantName = ""
        this.formattedMerchantAddress = ""
        this.transactionType = ""
        this.approvalText = ""
        this.aid = ""
        this.applicationLabel = ""
        this.applicationPreferredName = ""
        this.association = ""
        this.isoResponseCode = ""
        this.cryptogram = ""
        this.cryptogramType = ""
        this.tvr = ""
        this.date = ""
        this.time = ""
        this.maskedPan = ""
    }
}

class TT3TransactionDetails {
    var qrCodeState: String
    var transactionId: String
    var merchantTransactionReference: String
    var userId: String
    var status: String
    var disposition: String
    var amount: String
    var currency: String
    var type: String
    var responseCode: String
    var authorisationCode: String
    var createdAt: String
    var updatedAt: String
    var accountNumber: String
    var idNumber: String
    var creditorABSN: String
    var maxCollectionAmount: String
    var contractReference: String
    var collectionDay: String

    constructor(
        qrCodeState: String,
        transactionId: String,
        merchantTransactionReference: String,
        userId: String,
        status: String,
        disposition: String,
        amount: String,
        currency: String,
        type: String,
        responseCode: String,
        authorisationCode: String,
        createdAt: String,
        updatedAt: String,
        accountNumber: String,
        idNumber: String,
        creditorABSN: String,
        maxCollectionAmount: String,
        contractReference: String,
        collectionDay: String,
    ) {
        this.qrCodeState = qrCodeState
        this.transactionId = transactionId
        this.merchantTransactionReference = merchantTransactionReference
        this.userId = userId
        this.status = status
        this.disposition = disposition
        this.amount = amount
        this.currency = currency
        this.type = type
        this.responseCode = responseCode
        this.authorisationCode = authorisationCode
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.accountNumber = accountNumber
        this.idNumber = idNumber
        this.creditorABSN = creditorABSN
        this.maxCollectionAmount = maxCollectionAmount
        this.contractReference = contractReference
        this.collectionDay = collectionDay
    }

    constructor() {
        this.qrCodeState = ""
        this.transactionId = ""
        this.merchantTransactionReference = ""
        this.userId = ""
        this.status = ""
        this.disposition = ""
        this.amount = ""
        this.currency = ""
        this.type = ""
        this.responseCode = ""
        this.authorisationCode = ""
        this.createdAt = ""
        this.updatedAt = ""
        this.accountNumber = ""
        this.idNumber = ""
        this.creditorABSN = ""
        this.maxCollectionAmount = ""
        this.contractReference = ""
        this.collectionDay = ""
    }
}