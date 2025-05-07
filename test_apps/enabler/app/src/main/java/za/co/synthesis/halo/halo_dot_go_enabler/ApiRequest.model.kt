package za.co.synthesis.halo.halo_dot_go_enabler

class ApiRequest(transactionType: String, invokingMethod: String, name: String, amount: Double?, paymentReference: String, accountNumber: String, idNumber: String, maxCollectionAmount: String, debitOrderDay: Int?, creditor: String, contractReference: String, instalmentAmount: Double?, instalmentVisibility: String?) {
    var transactionType = transactionType
    var invokingMethod = invokingMethod
    var name = name
    var amount = amount
    var paymentReference = paymentReference
    var accountNumber = accountNumber
    var idNumber = idNumber
    var maxCollectionAmount = maxCollectionAmount
    var debitOrderDay = debitOrderDay
    var creditor = creditor
    var contractReference = contractReference
    var instalmentAmount = instalmentAmount
    var instalmentVisibility = instalmentVisibility
}