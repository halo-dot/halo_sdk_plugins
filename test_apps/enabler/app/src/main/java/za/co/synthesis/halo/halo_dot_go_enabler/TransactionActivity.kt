package za.co.synthesis.halo.halo_dot_go_enabler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionActivity : AppCompatActivity() {

    private val api: Api by lazy { Api(this) }
    lateinit var activeProfile: Profile
    lateinit var transactionType: String
    private var deeplinkReference: String = ""
    private var applinkReference: String = ""
    private var intentReference: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        setSupportActionBar(findViewById(R.id.transaction_toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()

        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
        }

        val transactionToolbarTitle = findViewById<TextView>(R.id.toolbar_title)

        transactionToolbarTitle.text = activeProfile.activeRequest?.name

        var requestTransactionType = findViewById<Button>(R.id.btnTransactionType)
        var requestInvokingMethod = findViewById<Button>(R.id.btnInvokingMethod)
        var requestNameEditText = findViewById<EditText>(R.id.request_name_et)
        var requestAmountEditText = findViewById<EditText>(R.id.request_amount_et)
        var requestPaymentReferenceEditText = findViewById<EditText>(R.id.request_reference_et)
        var requestAccountNumberEditText = findViewById<EditText>(R.id.request_acc_number_et)
        var requestIDNumberEditText = findViewById<EditText>(R.id.request_id_number_et)
        var requestMaxCollectionAmountEditText = findViewById<EditText>(R.id.request_max_collection_et)
        var requestDebitOrderDayEditText = findViewById<EditText>(R.id.request_debit_order_day_et)
        var requestCreditorEditText = findViewById<EditText>(R.id.request_creditor_et)
        var requestContractReferenceEditText = findViewById<EditText>(R.id.request_contract_reference_et)
        var instalmentAmountReferenceEditText = findViewById<EditText>(R.id.request_instalment_amount_et)
        var instalmentVisibilityEditText = findViewById<EditText>(R.id.request_instalment_visibility_et)

        val startButton = findViewById<Button>(R.id.start_btn)
        val cancelButton = findViewById<Button>(R.id.cancel_btn)

        requestAmountEditText.visibility = View.GONE
        requestPaymentReferenceEditText.visibility = View.GONE
        requestAccountNumberEditText.visibility = View.GONE
        requestIDNumberEditText.visibility = View.GONE
        requestMaxCollectionAmountEditText.visibility = View.GONE
        requestDebitOrderDayEditText.visibility = View.GONE
        requestCreditorEditText.visibility = View.GONE
        requestContractReferenceEditText.visibility = View.GONE
        instalmentAmountReferenceEditText.visibility = View.GONE
        instalmentVisibilityEditText.visibility = View.GONE

        requestTransactionType.text = activeProfile.activeRequest?.transactionType
        requestInvokingMethod.text = activeProfile.activeRequest?.invokingMethod
        requestNameEditText.setText(activeProfile.activeRequest?.name)

        if (activeProfile.activeRequest?.transactionType == "Card Payments") {
            requestAmountEditText.visibility = View.VISIBLE
            requestPaymentReferenceEditText.visibility = View.VISIBLE

            requestAmountEditText.setText(activeProfile.activeRequest?.amount.toString())
            requestPaymentReferenceEditText.setText(activeProfile.activeRequest?.paymentReference)
        } else if (activeProfile.activeRequest?.transactionType == "Debicheck/TT3") {
            requestAccountNumberEditText.visibility = View.VISIBLE
            requestIDNumberEditText.visibility = View.VISIBLE
            requestMaxCollectionAmountEditText.visibility = View.VISIBLE
            requestDebitOrderDayEditText.visibility = View.VISIBLE
            requestCreditorEditText.visibility = View.VISIBLE
            requestContractReferenceEditText.visibility = View.VISIBLE
            instalmentAmountReferenceEditText.visibility = View.VISIBLE
            instalmentVisibilityEditText.visibility = View.VISIBLE

            requestAccountNumberEditText.setText(activeProfile.activeRequest?.accountNumber)
            requestIDNumberEditText.setText(activeProfile.activeRequest?.idNumber)
            requestMaxCollectionAmountEditText.setText(activeProfile.activeRequest?.maxCollectionAmount)
            requestDebitOrderDayEditText.setText(activeProfile.activeRequest?.debitOrderDay.toString())
            requestCreditorEditText.setText(activeProfile.activeRequest?.creditor)
            requestContractReferenceEditText.setText(activeProfile.activeRequest?.contractReference)
            instalmentAmountReferenceEditText.setText(activeProfile.activeRequest?.instalmentAmount.toString())
            instalmentVisibilityEditText.setText(activeProfile.activeRequest?.instalmentVisibility.toString())
        }

        startButton.setOnClickListener {
            if(activeProfile.activeRequest?.transactionType == "Card Payments" && activeProfile.activeRequest?.invokingMethod == "Android Intents") {
                cardPaymentsIntent()
            } else if(activeProfile.activeRequest?.transactionType == "Card Payments" && activeProfile.activeRequest?.invokingMethod == "Deeplinking") {
                cardPaymentsDeeplink()
            } else if(activeProfile.activeRequest?.transactionType == "Card Payments" && activeProfile.activeRequest?.invokingMethod == "Applinks") {
                cardPaymentsApplink()
            } else if(activeProfile.activeRequest?.transactionType == "Debicheck/TT3" && activeProfile.activeRequest?.invokingMethod == "Android Intents") {
                debicheckIntent()
            } else if(activeProfile.activeRequest?.transactionType == "Debicheck/TT3" && activeProfile.activeRequest?.invokingMethod == "Deeplinking") {
                debicheckDeeplink()
            } else if(activeProfile.activeRequest?.transactionType == "Debicheck/TT3" && activeProfile.activeRequest?.invokingMethod == "Applinks") {
                debicheckApplink()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun isHaloInstalled(): Boolean {
        val activities = packageManager.queryIntentActivities(Intent(HALO_ACTION), 0)
        if (activities.size > 0)
            activities.forEach { println(it.activityInfo) }
        return activities.size > 0
    }

    private fun cardPaymentsIntent() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postIntentTransaction(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.paymentReference!!,
            activeProfile.activeRequest?.amount!!,
            "ZAR",
        ) { id, merchantJwt ->
            intentReference = id
            if (id.isNotEmpty()) {
                openHaloAppForTransaction(id, merchantJwt, null, "cardPaymentsIntent")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_transaction_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun cardPaymentsDeeplink() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postQrCode(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.paymentReference!!,
            activeProfile.activeRequest?.amount!!,
            "ZAR",
            false,
            false
        ) { url, reference ->
            deeplinkReference = reference
            if(url.isNotEmpty()){
                openHaloAppForTransaction(null, null, url, "cardPaymentsDeeplink")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_deeplink_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun cardPaymentsApplink() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postApplink(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.paymentReference!!,
            activeProfile.activeRequest?.amount!!,
            "ZAR",
            false,
            false
        ) { url, reference ->
            applinkReference = reference
            if(url.isNotEmpty()){
                openHaloAppForTransaction(null, null, url, "cardPaymentsApplink")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_deeplink_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun debicheckIntent() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postIntentTT3Transaction(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.accountNumber!!,
            activeProfile.activeRequest?.idNumber!!,
            activeProfile.activeRequest?.maxCollectionAmount!!,
            activeProfile.activeRequest?.contractReference!!,
            false,
            activeProfile.activeRequest?.debitOrderDay!!.toString(),
            activeProfile.activeRequest?.creditor!!,
            activeProfile.activeRequest?.instalmentAmount.toString()!!,
            activeProfile.activeRequest?.instalmentVisibility.toString()!!,
        )  { id, merchantJwt ->
            intentReference = id
            if (id.isNotEmpty()) {
                openHaloAppForTransaction(id, merchantJwt, null, "debicheckIntent")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_transaction_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun debicheckDeeplink() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postTT3QrCode(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.accountNumber!!,
            activeProfile.activeRequest?.debitOrderDay.toString()!!,
            activeProfile.activeRequest?.creditor!!,
            activeProfile.activeRequest?.idNumber!!,
            activeProfile.activeRequest?.maxCollectionAmount!!,
            activeProfile.activeRequest?.contractReference!!,
            activeProfile.activeRequest?.instalmentAmount.toString()!!,
            activeProfile.activeRequest?.instalmentVisibility.toString()!!,
            false,
            false
        ) { url, reference ->
            deeplinkReference = reference
            if(url.isNotEmpty()){
                openHaloAppForTransaction(null, null, url, "debicheckDeeplink")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_deeplink_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun debicheckApplink() {
        if (!isHaloInstalled()) {
            Toast.makeText(this, resources.getString(R.string.halo_installed_error), Toast.LENGTH_LONG).show()
            return
        }

        api.postTT3Applink(
            activeProfile.merchantId!!,
            activeProfile.activeRequest?.accountNumber!!,
            activeProfile.activeRequest?.debitOrderDay.toString()!!,
            activeProfile.activeRequest?.creditor!!,
            activeProfile.activeRequest?.idNumber!!,
            activeProfile.activeRequest?.maxCollectionAmount!!,
            activeProfile.activeRequest?.contractReference!!,
            activeProfile.activeRequest?.instalmentAmount.toString()!!,
            activeProfile.activeRequest?.instalmentVisibility.toString()!!,
            false,
            false
        ) { url, reference ->
            applinkReference = reference
            if(url.isNotEmpty()){
                openHaloAppForTransaction(null, null, url, "debicheckApplink")
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.retrieve_deeplink_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun openHaloAppForTransaction(transactionId: String?, jwt: String?, url: String?, type: String) {
        if (type == "cardPaymentsIntent") {
            transactionType = "cardPaymentsIntent"
            val haloPaymentIntent = Intent(HALO_ACTION).apply {
                putExtra("is_tap", true)
                putExtra("transaction_id", transactionId)
                putExtra("jwt", jwt)
            }
            startActivityForResult(haloPaymentIntent, HALO_REQUEST_CODE)
        } else if (type == "cardPaymentsDeeplink") {
            transactionType = "cardPaymentsDeeplink"
            val uri = Uri.parse(url)
            // startActivity(Intent(Intent.ACTION_VIEW, uri))
            startActivityForResult(Intent(Intent.ACTION_VIEW, uri), HALO_REQUEST_CODE)
        } else if (type == "cardPaymentsApplink") {
            transactionType = "cardPaymentsApplink"
            val uri = Uri.parse(url)
            startActivityForResult(Intent(Intent.ACTION_VIEW, uri), HALO_REQUEST_CODE)
        } else if (type == "debicheckIntent") {
            transactionType = "debicheckIntent"
            val haloPaymentIntent = Intent(HALO_ACTION).apply {
                putExtra("is_tap", false)
                putExtra("transaction_id", transactionId)
                putExtra("jwt", jwt)

                putExtra("accountNumber", activeProfile.activeRequest?.accountNumber!!)
                putExtra("maxCollectionAmount", activeProfile.activeRequest?.maxCollectionAmount!!)
                putExtra("pid", activeProfile.activeRequest?.idNumber!!)
                putExtra("contractReference", activeProfile.activeRequest?.contractReference!!)
                putExtra("collectionDay", activeProfile.activeRequest?.debitOrderDay!!.toString())
                putExtra("creditorABSN", activeProfile.activeRequest?.creditor!!)
                putExtra("instalmentAmount", activeProfile.activeRequest?.instalmentAmount.toString()!!)
                putExtra("instalmentVisibility", activeProfile.activeRequest?.instalmentVisibility.toString()!!)
            }
            startActivityForResult(haloPaymentIntent, HALO_REQUEST_CODE)
        } else if (type == "debicheckDeeplink") {
            transactionType = "debicheckDeeplink"
            val uri = Uri.parse(url)
            // startActivity(Intent(Intent.ACTION_VIEW, uri))
            startActivityForResult(Intent(Intent.ACTION_VIEW, uri), HALO_REQUEST_CODE)
        } else if (type == "debicheckApplink") {
            transactionType = "debicheckApplink"
            val uri = Uri.parse(url)
            startActivityForResult(Intent(Intent.ACTION_VIEW, uri), HALO_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == HALO_REQUEST_CODE) {
            val intent = Intent(this, TransactionCompletedActivity::class.java)

            if (resultCode == Activity.RESULT_OK) {
                val transactionId = data?.getStringExtra("result_transaction_id")

                if(transactionType == "debicheckIntent") {
                    api.getTT3TransactionDetails(
                        intentReference
                    ) { transactionDetail ->
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        val tt3TransactionDetails = Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                        if (tt3TransactionDetails.disposition == "Approved") {
                            intent.putExtra("TransactionId", intentReference)
                            intent.putExtra("TransactionResult", "success")
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        } else {
                            intent.putExtra("TransactionId", intentReference)
                            intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        }
                    }
                } else {
                    intent.putExtra("TransactionId", transactionId)
                    intent.putExtra("TransactionResult", "success")
                    intent.putExtra("TransactionType", transactionType)
                    startActivity(intent)
                }
            } else if (deeplinkReference != "") {
                if (transactionType == "cardPaymentsDeeplink") {
                    api.getTT3TransactionDetails(
                        deeplinkReference
                    ) { transactionDetail ->
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        val tt3TransactionDetails = Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                        if (tt3TransactionDetails.disposition == "Approved") {
                            intent.putExtra("TransactionId", tt3TransactionDetails.transactionId)
                            intent.putExtra("TransactionResult", "success")
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        } else {
                            intent.putExtra("TransactionId", tt3TransactionDetails.transactionId)
                            intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        }
                    }
                } else if (transactionType == "debicheckDeeplink") {
                    api.getTT3TransactionDetails(
                        deeplinkReference
                    ) { transactionDetail ->
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        val tt3TransactionDetails = Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                        if (tt3TransactionDetails.disposition == "Approved") {
                            intent.putExtra("TransactionId", deeplinkReference)
                            intent.putExtra("TransactionResult", "success")
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        } else {
                            intent.putExtra("TransactionId", deeplinkReference)
                            intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        }
                    }
                }
            } else if (applinkReference != "") {
                if (transactionType == "cardPaymentsApplink") {
                    api.getTT3TransactionDetails(
                        applinkReference
                    ) { transactionDetail ->
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        val tt3TransactionDetails = Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                        if (tt3TransactionDetails.disposition == "Approved") {
                            intent.putExtra("TransactionId", tt3TransactionDetails.transactionId)
                            intent.putExtra("TransactionResult", "success")
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        } else {
                            intent.putExtra("TransactionId", tt3TransactionDetails.transactionId)
                            intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        }
                    }
                } else if (transactionType == "debicheckApplink") {
                    api.getTT3TransactionDetails(
                        applinkReference
                    ) { transactionDetail ->
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        val tt3TransactionDetails = Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                        if (tt3TransactionDetails.disposition == "Approved") {
                            intent.putExtra("TransactionId", deeplinkReference)
                            intent.putExtra("TransactionResult", "success")
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        } else {
                            intent.putExtra("TransactionId", deeplinkReference)
                            intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                            intent.putExtra("TransactionType", transactionType)
                            startActivity(intent)
                        }
                    }
                }
            } else {
                api.getTT3TransactionDetails(
                    intentReference
                ) { transactionDetail ->
                    val tt3tdType = object : TypeToken<TT3TransactionDetails>() {}.type
                    val tt3TransactionDetails =
                        Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)

                    if(tt3TransactionDetails.disposition != "") {
                        intent.putExtra("TransactionId", tt3TransactionDetails.transactionId)
                        intent.putExtra("TransactionResult", tt3TransactionDetails.disposition)
                        intent.putExtra("TransactionType", transactionType)
                        startActivity(intent)
                    } else {
                        intent.putExtra("TransactionType", transactionType)
                        startActivity(intent)
                    }
                }
            }
            return
        }
    }

    companion object {
        private const val HALO_ACTION = "za.co.synthesis.halo.transaction"
        private const val HALO_REQUEST_CODE = 38
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}