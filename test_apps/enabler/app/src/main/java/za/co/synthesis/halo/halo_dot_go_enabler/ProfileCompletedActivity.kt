package za.co.synthesis.halo.halo_dot_go_enabler

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProfileCompletedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_completed)

        setSupportActionBar(findViewById(R.id.profile_completed_toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()

        val tempProfileGson = intent.getStringExtra("tempProfile")
        val tempRequestGson = intent.getStringExtra("tempRequest")
        val tempProfile = gson.fromJson<Profile>(tempProfileGson, Profile::class.java)
        val tempRequest = if (tempRequestGson == "") null else gson.fromJson<ApiRequest>(tempRequestGson, ApiRequest::class.java)

        val environmentImageView = findViewById<ImageView>(R.id.profile_env_iv)
        val profileNameValueTextView = findViewById<TextView>(R.id.profile_name_tv)
        val environmentValueTextView = findViewById<TextView>(R.id.profile_env_value_tv)
        val merchantIDValueTextView = findViewById<TextView>(R.id.profile_merchant_id_value_tv)
        val apiKeyTextView = findViewById<TextView>(R.id.profile_api_key_tv)
        val apiKeyValueTextView = findViewById<TextView>(R.id.profile_api_key_value_tv)
        val usernameTextView = findViewById<TextView>(R.id.profile_username_tv)
        val usernameValueTextView = findViewById<TextView>(R.id.profile_username_value_tv)
        val passwordTextView = findViewById<TextView>(R.id.profile_password_tv)
        val passwordValueTextView = findViewById<TextView>(R.id.profile_password_value_tv)

        when (tempProfile.haloEnvironment) {
            "dev" -> {
                environmentImageView.setImageResource(R.drawable.ic_env_dev)
            }
            "qa" -> {
                environmentImageView.setImageResource(R.drawable.ic_env_qa)
            }
            "prod" -> {
                environmentImageView.setImageResource(R.drawable.ic_env_prod)
            }
        }

        profileNameValueTextView.text = tempProfile.name
        environmentValueTextView.text = tempProfile.haloEnvironment
        merchantIDValueTextView.text = tempProfile.merchantId.toString()

        if(tempProfile.authPreference == "apikey") {
            apiKeyValueTextView.text = tempProfile.apiKey
            usernameTextView.visibility = View.GONE
            usernameValueTextView.visibility = View.GONE
            passwordTextView.visibility = View.GONE
            passwordValueTextView.visibility = View.GONE
        } else {
            apiKeyTextView.visibility = View.GONE
            apiKeyValueTextView.visibility = View.GONE
            usernameValueTextView.text = tempProfile.username
            passwordValueTextView.text = tempProfile.password
        }

        val requestNameTextView = findViewById<TextView>(R.id.profile_request_name_tv)
        val requestNameValueTextView = findViewById<TextView>(R.id.profile_request_name_value_tv)
        val transactionTypeTextView = findViewById<TextView>(R.id.profile_transaction_type_tv)
        val transactionTypeValueTextView = findViewById<TextView>(R.id.profile_transaction_type_value_tv)
        val invokingMethodTextView = findViewById<TextView>(R.id.profile_invoking_method_tv)
        val invokingMethodValueTextView = findViewById<TextView>(R.id.profile_invoking_method_value_tv)

        val amountTextView = findViewById<TextView>(R.id.profile_amount_tv)
        val amountValueTextView = findViewById<TextView>(R.id.profile_amount_value_tv)
        val paymentReferenceTextView = findViewById<TextView>(R.id.profile_payment_reference_tv)
        val paymentReferenceValueTextView = findViewById<TextView>(R.id.profile_payment_reference_value_tv)

        val accountNumberTextView = findViewById<TextView>(R.id.profile_account_number_tv)
        val accountNumberValueTextView = findViewById<TextView>(R.id.profile_account_number_value_tv)
        val idNumberTextView = findViewById<TextView>(R.id.profile_id_number_tv)
        val idNumberValueTextView = findViewById<TextView>(R.id.profile_id_number_value_tv)
        val maxCollectionAmountTextView = findViewById<TextView>(R.id.profile_max_collection_amount_tv)
        val maxCollectionAmountValueTextView = findViewById<TextView>(R.id.profile_max_collection_amount_value_tv)
        val debitOrderDayTextView = findViewById<TextView>(R.id.profile_debit_order_day_tv)
        val debitOrderDayValueTextView = findViewById<TextView>(R.id.profile_debit_order_day_value_tv)
        val creditorTextView = findViewById<TextView>(R.id.profile_creditor_tv)
        val creditorValueTextView = findViewById<TextView>(R.id.profile_creditor_value_tv)
        val contractReferenceNumberTextView = findViewById<TextView>(R.id.profile_contract_reference_number_tv)
        val contractReferenceNumberValueTextView = findViewById<TextView>(R.id.profile_contract_reference_number_value_tv)
        val instalmentAmountTextView = findViewById<TextView>(R.id.profile_instalment_amount_tv)
        val instalmentAmountValueTextView = findViewById<TextView>(R.id.profile_instalment_amount_value_tv)
        val instalmentVisibilityTextView = findViewById<TextView>(R.id.profile_instalment_visibility_tv)
        val instalmentVisibilityValueTextView = findViewById<TextView>(R.id.profile_instalment_visibility_value_tv)

        if(tempRequest == null) {
            requestNameTextView.visibility = View.GONE
            requestNameValueTextView.visibility = View.GONE
            transactionTypeTextView.visibility = View.GONE
            transactionTypeValueTextView.visibility = View.GONE
            invokingMethodTextView.visibility = View.GONE
            invokingMethodValueTextView.visibility = View.GONE
            amountTextView.visibility = View.GONE
            amountValueTextView.visibility = View.GONE
            paymentReferenceTextView.visibility = View.GONE
            paymentReferenceValueTextView.visibility = View.GONE
            accountNumberTextView.visibility = View.GONE
            accountNumberValueTextView.visibility = View.GONE
            idNumberTextView.visibility = View.GONE
            idNumberValueTextView.visibility = View.GONE
            maxCollectionAmountTextView.visibility = View.GONE
            maxCollectionAmountValueTextView.visibility = View.GONE
            debitOrderDayTextView.visibility = View.GONE
            debitOrderDayValueTextView.visibility = View.GONE
            creditorTextView.visibility = View.GONE
            creditorValueTextView.visibility = View.GONE
            contractReferenceNumberTextView.visibility = View.GONE
            contractReferenceNumberValueTextView.visibility = View.GONE
            instalmentAmountTextView.visibility = View.GONE
            instalmentAmountValueTextView.visibility = View.GONE
            instalmentVisibilityTextView.visibility = View.GONE
            instalmentVisibilityValueTextView.visibility = View.GONE
        } else {
            if(tempRequest.transactionType == "Card Payments") {
                requestNameTextView.visibility = View.VISIBLE
                requestNameValueTextView.visibility = View.VISIBLE
                transactionTypeTextView.visibility = View.VISIBLE
                transactionTypeValueTextView.visibility = View.VISIBLE
                invokingMethodTextView.visibility = View.VISIBLE
                invokingMethodValueTextView.visibility = View.VISIBLE
                amountTextView.visibility = View.VISIBLE
                amountValueTextView.visibility = View.VISIBLE
                paymentReferenceTextView.visibility = View.VISIBLE
                paymentReferenceValueTextView.visibility = View.VISIBLE

                requestNameValueTextView.text = tempRequest.name
                transactionTypeValueTextView.text = tempRequest.transactionType
                invokingMethodValueTextView.text = tempRequest.invokingMethod
                amountValueTextView.text = getString(R.string.amount_value_textview, tempRequest.amount.toString())
                paymentReferenceValueTextView.text = tempRequest.paymentReference

                accountNumberTextView.visibility = View.GONE
                accountNumberValueTextView.visibility = View.GONE
                idNumberTextView.visibility = View.GONE
                idNumberValueTextView.visibility = View.GONE
                maxCollectionAmountTextView.visibility = View.GONE
                maxCollectionAmountValueTextView.visibility = View.GONE
                debitOrderDayTextView.visibility = View.GONE
                debitOrderDayValueTextView.visibility = View.GONE
                creditorTextView.visibility = View.GONE
                creditorValueTextView.visibility = View.GONE
                contractReferenceNumberTextView.visibility = View.GONE
                contractReferenceNumberValueTextView.visibility = View.GONE
                instalmentAmountTextView.visibility = View.GONE
                instalmentAmountValueTextView.visibility = View.GONE
                instalmentVisibilityTextView.visibility = View.GONE
                instalmentVisibilityValueTextView.visibility = View.GONE
            } else {
                requestNameTextView.visibility = View.VISIBLE
                requestNameValueTextView.visibility = View.VISIBLE
                transactionTypeTextView.visibility = View.VISIBLE
                transactionTypeValueTextView.visibility = View.VISIBLE
                invokingMethodTextView.visibility = View.VISIBLE
                invokingMethodValueTextView.visibility = View.VISIBLE
                accountNumberTextView.visibility = View.VISIBLE
                accountNumberValueTextView.visibility = View.VISIBLE
                idNumberTextView.visibility = View.VISIBLE
                idNumberValueTextView.visibility = View.VISIBLE
                maxCollectionAmountTextView.visibility = View.VISIBLE
                maxCollectionAmountValueTextView.visibility = View.VISIBLE
                debitOrderDayTextView.visibility = View.VISIBLE
                debitOrderDayValueTextView.visibility = View.VISIBLE
                creditorTextView.visibility = View.VISIBLE
                creditorValueTextView.visibility = View.VISIBLE
                contractReferenceNumberTextView.visibility = View.VISIBLE
                contractReferenceNumberValueTextView.visibility = View.VISIBLE
                instalmentAmountTextView.visibility = View.VISIBLE
                instalmentAmountValueTextView.visibility = View.VISIBLE
                instalmentVisibilityTextView.visibility = View.VISIBLE
                instalmentVisibilityValueTextView.visibility = View.VISIBLE

                requestNameValueTextView.text = tempRequest.name
                transactionTypeValueTextView.text = tempRequest.transactionType
                invokingMethodValueTextView.text = tempRequest.invokingMethod
                accountNumberValueTextView.text = tempRequest.accountNumber
                idNumberValueTextView.text = tempRequest.idNumber
                maxCollectionAmountValueTextView.text = tempRequest.maxCollectionAmount
                debitOrderDayValueTextView.text = tempRequest.debitOrderDay.toString()
                creditorValueTextView.text = tempRequest.creditor
                contractReferenceNumberValueTextView.text = tempRequest.contractReference
                instalmentAmountValueTextView.text = getString(R.string.amount_value_textview, tempRequest.instalmentAmount.toString())
                instalmentVisibilityValueTextView.text = tempRequest.instalmentVisibility.toString()

                amountTextView.visibility = View.GONE
                amountValueTextView.visibility = View.GONE
                paymentReferenceTextView.visibility = View.GONE
                paymentReferenceValueTextView.visibility = View.GONE
            }
        }

        val saveButton = findViewById<Button>(R.id.save_btn)
        saveButton.setOnClickListener {
            if (tempRequest != null) {
                tempProfile.apiRequests = mutableListOf<ApiRequest>()
                tempProfile.apiRequests?.add(tempRequest)
            }

            var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
            val jsonProfiles: String? = sharedPreferences.getString("Profiles", "")
            var profiles /*: MutableList<Profile>*/ = mutableListOf<Profile>()
            if (jsonProfiles != null && jsonProfiles != ""){
                val sType = object : TypeToken<List<Profile>>() { }.type
                profiles = gson.fromJson<MutableList<Profile>>(jsonProfiles, sType)
            }

            profiles.add(tempProfile)
            val jsonProfilesNew = gson.toJson(profiles)

            val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()

            prefsEditor.remove("ActiveProfile")
            prefsEditor.commit()
            prefsEditor.apply()

            prefsEditor.putString("Profiles", jsonProfilesNew)
            prefsEditor.putString("ActiveProfile", gson.toJson(tempProfile))
            prefsEditor.commit()
            prefsEditor.apply()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
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