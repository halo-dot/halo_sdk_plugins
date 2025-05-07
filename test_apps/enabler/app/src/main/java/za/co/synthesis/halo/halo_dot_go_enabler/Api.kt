package za.co.synthesis.halo.halo_dot_go_enabler

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import net.minidev.json.JSONObject
import net.minidev.json.JSONStyle
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Api (private val context: Context) {
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().run {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
//            sslSocketFactory(
//                sslContext.socketFactory,
//                trustManagers.single { tm -> tm is X509TrustManager } as X509TrustManager
//            )
            build()
        }
    }

    val gson = Gson()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", Context.MODE_PRIVATE)

    val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
    var activeProfile: Profile = gson.fromJson(jsonActiveProfile, Profile::class.java)
    val env = setEnv(activeProfile.haloEnvironment)

    fun setEnv(haloEnv: String): String {
        if(haloEnv == "dev") {
            return "za.dev"
        } else if(haloEnv == "qa") {
            return "qa"
        } else if(haloEnv == "prod") {
            return "prod"
        }

        return ""
    }

    private var lastTokenTime: Long? = null
    private var token: String? = null

    fun setToken(token: String?) {
        this.token = token
    }

    fun loginForJWT(userName: String, password: String, callback: (String) -> Unit) {
        val nowMillis = Calendar.getInstance().timeInMillis
        if (token != null && lastTokenTime != null && (nowMillis - lastTokenTime!! < 14 * 60 * 1000L)) {
            callback(token!!)
            return
        }
        val request = Request.Builder().run {
            url("https://authserver.${env}.haloplus.io/login")
            val actualBody: String = JSONObject().apply {
                put("username", userName)
                put("password", password)
            }.toString(JSONStyle.NO_COMPRESS)
            val requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                actualBody
            )
            method("POST", requestBody)
            build()
        }
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error getting jwt via login call -> $e")
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Login request has failed: $e",
                        Toast.LENGTH_LONG
                    ).show()
                }
                callback("")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()?.string() ?: ""
                val responseCode = response.code()
                response.body()?.close()

                when {
                    responseBody.isEmpty() -> {
                        println("Error getting jwt via login call -> EMPTY RESPONSE")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Login request failed: EMPTY RESPONSE",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("")
                    }
                    responseCode != 200 -> {
                        println("responseCode = $responseCode")
                        println("responseBody = $responseBody")
                        callback("")
                    }
                    else -> {
                        val token = responseBody.parseBodyWithOneElement("token")
                        println("New token = $token")
                        lastTokenTime = Calendar.getInstance().timeInMillis
                        this@Api.token = token
                        callback(token)
                    }
                }
            }
        })
    }

    fun postIntentTransaction(merchantId: String, paymentReference: String, amount: Double, currencyCode: String, callback: (String, String) -> Unit) {
        var request: Request? = null

        if(activeProfile.authPreference == "apikey"){
            request = Request.Builder().run {
                url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/intentTransaction")
                val actualBody: String = JSONObject().apply {
                    put("merchantId", merchantId)
                    put("paymentReference", paymentReference)
                    put("amount", amount)
                    put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                    put("currencyCode", currencyCode)
                }.toString(JSONStyle.NO_COMPRESS)
                var requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    actualBody
                )
                method("POST", requestBody)
                header("x-api-key", activeProfile.apiKey)
//                .addHeader("Authorization", "Bearer $token")
                build()
            }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error postIntentTransaction -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Post Intent Transaction failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("", "")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error postIntentTransaction -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Post Intent Transaction request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("", "")
                        }
                        responseCode != 201 -> {
                            val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                            println("httpStatusCode = ${errorBody[0]}")
                            println("errorCode = ${errorBody[1]}")
                            println("message = ${errorBody[2]}")
                            if (errorBody[0].toInt() == 403){
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "${errorBody[2]}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("", "")
                            } else {
                                println("Post intent transaction failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("", "")
                            }
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            val postIntentTransaction = responseBody.parseBodyWithTwoElements("responseBody","id", "token")
                            println("Intent transaction id = ${postIntentTransaction[0]}")
                            println("Intent token id = ${postIntentTransaction[1]}")
                            callback(postIntentTransaction[0],postIntentTransaction[1])
                        }
                    }
                }
            })
        } else{
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()) { jwt ->
                Log.v("JWT AFTER LOGIN",jwt.toString())
                Log.v("TOKEN AFTER LOGIN", token.toString())
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/intentTransaction")
                    val actualBody: String = JSONObject().apply {
                        put("merchantId", merchantId)
                        put("paymentReference", paymentReference)
                        put("amount", amount)
                        put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                        put("currencyCode", currencyCode)
                    }.toString(JSONStyle.NO_COMPRESS)
                    var requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        actualBody
                    )
                    method("POST", requestBody)
//                    header("x-api-key", API_KEY)
                        .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error postIntentTransaction -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Post Intent Transaction failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("", "")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()

                        when {
                            responseBody.isEmpty() -> {
                                println("Error postIntentTransaction -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Post Intent Transaction request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("", "")
                            }
                            responseCode != 201 -> {
                                val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                                println("httpStatusCode = ${errorBody[0]}")
                                println("errorCode = ${errorBody[1]}")
                                println("message = ${errorBody[2]}")
                                if (errorBody[0].toInt() == 403){
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            context,
                                            "${errorBody[2]}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    callback("", "")
                                } else {
                                    println("Post intent transaction failed")
                                    println("responseCode = $responseCode")
                                    println("responseBody = $responseBody")
                                    callback("", "")
                                }
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                val postIntentTransaction = responseBody.parseBodyWithTwoElements("responseBody","id", "token")
                                println("Intent transaction id = ${postIntentTransaction[0]}")
                                println("Intent token id = ${postIntentTransaction[1]}")
                                callback(postIntentTransaction[0],postIntentTransaction[1])
                            }
                        }
                    }
                })
            }
        }
    }

    fun postIntentTT3Transaction(merchantId: String, accountNumber: String, id: String, maxCollectionAmount: String, contractReference: String, isConsumerApp: Boolean, collectionDay: String, creditorABSN: String, instalmentAmount: String, instalmentVisibility: String, callback: (String, String) -> Unit) {
        var request: Request? = null

        if (activeProfile.authPreference == "apikey"){
            request = Request.Builder().run {
                url("https://kernelserver.${env}.haloplus.io/1.0.5/consumer/tt3IntentTransaction")
                val actualBody: String = JSONObject().apply {
                    put("merchantId", merchantId)
                    put("accountNumber", accountNumber)
                    put("id", id)
                    put("maxCollectionAmount", maxCollectionAmount)
                    put("contractReference", contractReference)
                    put("collectionDay", collectionDay)
                    put("creditorABSN", creditorABSN)
                    put("instalmentAmount", instalmentAmount)
                    put("instalmentVisibility", instalmentVisibility)
                    put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(
                        Date()
                    ))
                    put("isConsumerApp", isConsumerApp)
                }.toString(JSONStyle.NO_COMPRESS)
                var requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    actualBody
                )
                method("POST", requestBody)
                header("x-api-key", activeProfile.apiKey)
//                .addHeader("Authorization", "Bearer $token")
                build()
            }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error postIntentTransaction -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Post Intent Transaction failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("", "")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error postIntentTransaction -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Post Intent Transaction request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("", "")
                        }
                        responseCode != 201 -> {
                            println("Post intent transaction failed")
                            println("responseCode = $responseCode")
                            println("responseBody = $responseBody")
                            callback("", "")
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            val postIntentTransaction = responseBody.parseBodyWithTwoElements("responseBody","id", "token")
                            println("Intent transaction id = ${postIntentTransaction[0]}")
                            println("Intent token id = ${postIntentTransaction[1]}")
                            callback(postIntentTransaction[0],postIntentTransaction[1])
                        }
                    }
                }
            })
        }
        else{
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()) { jwt ->
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/1.0.5/consumer/tt3IntentTransaction")
                    val actualBody: String = JSONObject().apply {
                        put("merchantId", merchantId)
                        put("accountNumber", accountNumber)
                        put("id", id)
                        put("maxCollectionAmount", maxCollectionAmount)
                        put("contractReference", contractReference)
                        put("collectionDay", collectionDay)
                        put("creditorABSN", creditorABSN)
                        put("instalmentAmount", instalmentAmount)
                        put("instalmentVisibility", instalmentVisibility)
                        put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(
                            Date()
                        ))
                        put("isConsumerApp", isConsumerApp)
                    }.toString(JSONStyle.NO_COMPRESS)
                    var requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        actualBody
                    )
                    method("POST", requestBody)
//                    header("x-api-key", API_KEY)
                .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error postIntentTransaction -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Post Intent Transaction failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("", "")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()

                        when {
                            responseBody.isEmpty() -> {
                                println("Error postIntentTransaction -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Post Intent Transaction request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("", "")
                            }
                            responseCode != 201 -> {
                                println("Post intent transaction failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("", "")
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                val postIntentTransaction = responseBody.parseBodyWithTwoElements("responseBody","id", "token")
                                println("Intent transaction id = ${postIntentTransaction[0]}")
                                println("Intent token id = ${postIntentTransaction[1]}")
                                callback(postIntentTransaction[0],postIntentTransaction[1])
                            }
                        }
                    }
                })
            }
        }
    }

    fun postQrCode(merchantId: String, paymentReference: String, amount: Double, currencyCode: String, isConsumerApp: Boolean, imageRequired: Boolean, callback: (String, String) -> Unit){
        var request: Request? = null

        val isImageRequired: JSONObject = JSONObject().apply {
            put("required", imageRequired)
        }

        if(activeProfile.authPreference == "apikey"){
            request = Request.Builder().run {
                url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/qrCode")
                val actualBody: String = JSONObject().apply {
                    put("merchantId", merchantId)
                    put("paymentReference", paymentReference)
                    put("amount", amount)
                    put("currencyCode", currencyCode)
                    put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                    put("isConsumerApp", isConsumerApp)
                    put("image", isImageRequired)
                }.toString(JSONStyle.NO_COMPRESS)
                var requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    actualBody
                )
                method("POST", requestBody)
                header("x-api-key", activeProfile.apiKey)
//                .addHeader("Authorization", "Bearer $token")
                build()
            }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error generate qrCode -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Generate QR Code failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("", "")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error generate qrCode -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Generate QR Code request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("", "")
                        }
                        responseCode != 201 -> {
                            println("Generate qr code failed")
                            println("responseCode = $responseCode")
                            println("responseBody = $responseBody")
                            callback("", "")
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            val postQRCode = responseBody.parseBodyWithTwoElements("responseBody", "url", "reference")
                            println("Generated QR Code url = ${postQRCode[0]}")
                            println("Reference = ${postQRCode[1]}")
                            callback(postQRCode[0], postQRCode[1])
                        }
                    }
                }
            })
        } else {
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()){ jwt ->
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/qrCode")
                    val actualBody: String = JSONObject().apply {
                        put("merchantId", merchantId)
                        put("paymentReference", paymentReference)
                        put("amount", amount)
                        put("currencyCode", currencyCode)
                        put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                        put("isConsumerApp", isConsumerApp)
                        put("image", isImageRequired)
                    }.toString(JSONStyle.NO_COMPRESS)
                    var requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        actualBody
                    )
                    method("POST", requestBody)
//                    header("x-api-key", API_KEY)
                        .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error generate qrCode -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Generate QR Code failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("", "")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()

                        when {
                            responseBody.isEmpty() -> {
                                println("Error generate qrCode -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Generate QR Code request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("", "")
                            }
                            responseCode != 201 -> {
                                println("Generate qr code failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("", "")
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                val postQRCode = responseBody.parseBodyWithTwoElements("responseBody", "url", "reference")
                                println("Generated QR Code url = ${postQRCode[0]}")
                                println("Reference = ${postQRCode[1]}")
                                callback(postQRCode[0], postQRCode[1])
                            }
                        }
                    }
                })
            }
        }
    }

    fun postTT3QrCode(merchantId: String, accountNumber: String, collectionDay: String, creditorABSN: String, id: String, maxCollectionAmount: String, contractReference: String, instalmentAmount: String, instalmentVisibility: String, isConsumerApp: Boolean, imageRequired: Boolean, callback: (String, String) -> Unit){
        var request: Request? = null

        val isImageRequired: JSONObject = JSONObject().apply {
            put("required", imageRequired)
        }

        if(activeProfile.authPreference == "apikey"){
             request = Request.Builder().run {
                 url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/tt3QRCode")
                 val actualBody: String = JSONObject().apply {
                     put("merchantId", merchantId)
                     put("accountNumber", accountNumber)
                     put("collectionDay", collectionDay)
                     put("creditorABSN", creditorABSN)
                     put("id", id)
                     put("maxCollectionAmount", maxCollectionAmount)
                     put("contractReference", contractReference)
                     put("instalmentAmount", instalmentAmount)
                     put("instalmentVisibility", instalmentVisibility)
                     put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                     put("isConsumerApp", isConsumerApp)
                     put("image", isImageRequired)
                 }.toString(JSONStyle.NO_COMPRESS)
                 var requestBody = RequestBody.create(
                     MediaType.parse("application/json"),
                     actualBody
                 )
                 method("POST", requestBody)
                 header("x-api-key", activeProfile.apiKey)
//                     .addHeader("Authorization", "Bearer $token")
                 build()
             }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error generate qrCode -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Generate QR Code failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("", "")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error generate qrCode -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Generate QR Code request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("", "")
                        }
                        responseCode != 201 -> {
                            println("Generate qr code failed")
                            println("responseCode = $responseCode")
                            println("responseBody = $responseBody")
                            callback("", "")
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            val postTT3QrCode = responseBody.parseBodyWithTwoElements("responseBody","url", "reference")
                            println("Generated QR Code url = ${postTT3QrCode[0]}")
                            println("Reference = ${postTT3QrCode[1]}")
                            callback(postTT3QrCode[0], postTT3QrCode[1])
                        }
                    }
                }
            })
        } else {
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()){ jwt ->
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/1.0.10/consumer/tt3QRCode")
                    val actualBody: String = JSONObject().apply {
                        put("merchantId", merchantId)
                        put("accountNumber", accountNumber)
                        put("collectionDay", collectionDay)
                        put("creditorABSN", creditorABSN)
                        put("id", id)
                        put("maxCollectionAmount", maxCollectionAmount)
                        put("contractReference", contractReference)
                        put("instalmentAmount", instalmentAmount)
                        put("instalmentVisibility", instalmentVisibility)
                        put("timestamp", SimpleDateFormat("E MMM d yyyy HH:mm:ss 'GMT'Z", Locale.US).format(Date()))
                        put("isConsumerApp", isConsumerApp)
                        put("image", isImageRequired)
                    }.toString(JSONStyle.NO_COMPRESS)
                    var requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        actualBody
                    )
                    method("POST", requestBody)
//            header("x-api-key", API_KEY)
                        .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error generate qrCode -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Generate QR Code failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("", "")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()

                        when {
                            responseBody.isEmpty() -> {
                                println("Error generate qrCode -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Generate QR Code request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("", "")
                            }
                            responseCode != 201 -> {
                                println("Generate qr code failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("", "")
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                val postTT3QrCode = responseBody.parseBodyWithTwoElements("responseBody","url", "reference")
                                println("Generated QR Code url = ${postTT3QrCode[0]}")
                                println("Reference = ${postTT3QrCode[1]}")
                                callback(postTT3QrCode[0], postTT3QrCode[1])
                            }
                        }
                    }
                })
            }
        }
    }

    fun postTransactionDetails(transactionId: String, callback: (String) -> Unit) {
        var request: Request? = null

        if(activeProfile.authPreference == "apikey"){
            request = Request.Builder().run {
                url("https://kernelserver.${env}.haloplus.io/transactions/${transactionId}")
                method("GET", null)
                    header("x-api-key", activeProfile.apiKey)
//                    .addHeader("Authorization", "Bearer $token")
                build()
            }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error postTransactionDetails -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Post Transaction Details failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error postTransactionDetails -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Post Transaction Details request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("")
                        }
                        responseCode == 401 -> {
                            callback("401")
                        }
                        responseCode != 200 -> {
                            val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                            println("httpStatusCode = ${errorBody[0]}")
                            println("errorCode = ${errorBody[1]}")
                            println("message = ${errorBody[2]}")
                            if (errorBody[0].toInt() == 403){
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "${errorBody[2]}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("")
                            } else {
                                println("Post Transaction Details failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("")
                            }
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            callback(responseBody)
                        }
                    }
                }
            })
        } else{
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()) { jwt ->
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/transactions/${transactionId}")
                    method("GET", null)
//                    header("x-api-key", API_KEY)
                        .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error postTransactionDetails -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Post Transaction Details failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()
                        println("responseBody = ${responseBody}")
                        println("responseCode = ${responseCode}")

                        when {
                            responseBody.isEmpty() -> {
                                println("Error postTransactionDetails -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Post Transaction Details request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("")
                            }
                            responseCode == 401 -> {
                                callback("401")
                            }
                            responseCode != 200 -> {
                                val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                                println("httpStatusCode = ${errorBody[0]}")
                                println("errorCode = ${errorBody[1]}")
                                println("message = ${errorBody[2]}")
                                if (errorBody[0].toInt() == 403){
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            context,
                                            "${errorBody[2]}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    callback("")
                                } else {
                                    println("Post transaction details failed")
                                    println("responseCode = $responseCode")
                                    println("responseBody = $responseBody")
                                    callback("")
                                }
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                callback(responseBody)
                            }
                        }
                    }
                })
            }
        }
    }

    fun getTT3TransactionDetails(transactionId: String, callback: (String) -> Unit) {
        var request: Request? = null

        if(activeProfile.authPreference == "apikey"){
            request = Request.Builder().run {
                url("https://kernelserver.${env}.haloplus.io/1.0.12/consumer/tt3QRCode/${transactionId}")
                method("GET", null)
                header("x-api-key", activeProfile.apiKey)
                build()
            }

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Error postTransactionDetails -> $e")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Post Transaction Details failed $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    callback("")
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body()?.string() ?: ""
                    val responseCode = response.code()
                    response.body()?.close()

                    when {
                        responseBody.isEmpty() -> {
                            println("Error postTransactionDetails -> EMPTY RESPONSE")
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(
                                    context,
                                    "Post Transaction Details request has failed: EMPTY RESPONSE",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            callback("")
                        }
                        responseCode == 401 -> {
                            callback("401")
                        }
                        responseCode != 200 -> {
                            val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                            println("httpStatusCode = ${errorBody[0]}")
                            println("errorCode = ${errorBody[1]}")
                            println("message = ${errorBody[2]}")
                            if (errorBody[0].toInt() == 403){
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "${errorBody[2]}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("")
                            } else {
                                println("Post Transaction Details failed")
                                println("responseCode = $responseCode")
                                println("responseBody = $responseBody")
                                callback("")
                            }
                        }
                        else -> {
                            println("FULL RESPONSE BODY = $responseBody")
                            callback(responseBody)
                        }
                    }
                }
            })
        } else{
            loginForJWT(activeProfile.username.toString(), activeProfile.password.toString()) { jwt ->
                request = Request.Builder().run {
                    url("https://kernelserver.${env}.haloplus.io/1.0.12/consumer/tt3QRCode/${transactionId}")
                    method("GET", null)
                        .addHeader("Authorization", "Bearer $token")
                    build()
                }

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Error postTransactionDetails -> $e")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Post Transaction Details failed $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        callback("")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val responseBody = response.body()?.string() ?: ""
                        val responseCode = response.code()
                        response.body()?.close()
                        println("responseBody = ${responseBody}")
                        println("responseCode = ${responseCode}")

                        when {
                            responseBody.isEmpty() -> {
                                println("Error postTransactionDetails -> EMPTY RESPONSE")
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        "Post Transaction Details request has failed: EMPTY RESPONSE",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                callback("")
                            }
                            responseCode == 401 -> {
                                callback("401")
                            }
                            responseCode != 200 -> {
                                val errorBody = responseBody.parseBodyWithThreeElements("responseBody","httpStatusCode", "errorCode", "message")
                                println("httpStatusCode = ${errorBody[0]}")
                                println("errorCode = ${errorBody[1]}")
                                println("message = ${errorBody[2]}")
                                if (errorBody[0].toInt() == 403){
                                    Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            context,
                                            "${errorBody[2]}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    callback("")
                                } else {
                                    println("Post transaction details failed")
                                    println("responseCode = $responseCode")
                                    println("responseBody = $responseBody")
                                    callback("")
                                }
                            }
                            else -> {
                                println("FULL RESPONSE BODY = $responseBody")
                                callback(responseBody)
                            }
                        }
                    }
                })
            }
        }
    }

}

fun String.parseBodyWithOneElement(elementName: String) : String {
    return try {
        this.replace("{","").replace("}", "").replace("\"","").replace("$elementName:", "")
    } catch (e: Exception) {
        println(e)
        ""
    }
}

fun String.parseBodyWithTwoElements(
    parseBodyWithTwoElements: String?,
    elementOne: String,
    elementTwo: String?
): List<String> {
    return try {
        val temp = this.replace("{","").replace("}", "").replace("\"","").replace(oldValue = "$elementOne:", newValue = "").replace(oldValue = "$elementTwo:", newValue = "")
        temp.split(",")
    } catch (e: Exception) {
        println(e)
        listOf("","")
    }
}

fun String.parseBodyWithThreeElements(
    parseBodyWithThreeElements: String?,
    elementOne: String,
    elementTwo: String?,
    elementThree: String?
): List<String> {
    return try {
        val temp = this.replace("{","").replace("}", "").replace("\"","").replace(oldValue = "$elementOne:", newValue = "").replace(oldValue = "$elementTwo:", newValue = "").replace(oldValue = "$elementThree:", newValue = "")
        temp.split(",")
    } catch (e: Exception) {
        println(e)
        listOf("","")
    }
}