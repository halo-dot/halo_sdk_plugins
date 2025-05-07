package za.co.synthesis.halo.halo_dot_go_enabler

class Profile(name: String, merchantId: String?, haloEnvironment: String, username: String, password: String, apiKey: String, authPreference: String, apiRequests: MutableList<ApiRequest>?, activeRequest: ApiRequest?) {
    var name = name
    var merchantId = merchantId
    var haloEnvironment = haloEnvironment
    var username = username
    var password = password
    var apiKey = apiKey
    var authPreference = authPreference
    var apiRequests = apiRequests
    var activeRequest = activeRequest
}