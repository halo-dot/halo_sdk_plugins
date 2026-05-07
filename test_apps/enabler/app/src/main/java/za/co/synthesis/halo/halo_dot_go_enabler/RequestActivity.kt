package za.co.synthesis.halo.halo_dot_go_enabler

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.DecimalFormat
import kotlin.random.Random
import kotlin.streams.asSequence


class RequestActivity : AppCompatActivity() {

    private var transactionTypeList: List<Dropdown> = listOf(
        Dropdown("Debicheck/TT3", R.drawable.ic_file_logo),
        Dropdown("Card Payments", R.drawable.ic_file_logo),
        Dropdown("Transaction Type", R.drawable.ic_file_logo)
    )

    private var invokingMethodList: List<Dropdown> = listOf(
        Dropdown("Android Intents", R.drawable.ic_link),
        Dropdown("Deeplinking", R.drawable.ic_link),
        Dropdown("Applinks", R.drawable.ic_link),
        Dropdown("Invoking Method", R.drawable.ic_link)
    )

    private var intentActionGlobal: String = ""
    private var requestToEditName: String = ""

    private var requestNameTouched = false
    private var requestAmountTouched = false
    private var requestPaymentReferenceTouched = false
    private var requestAccountNumberTouched = false
    private var requestIDNumberTouched = false
    private var requestMaxCollectionAmountTouched = false
    private var requestDebitOrderDayTouched = false
    private var requestCreditorTouched = false
    private var requestContractReferenceTouched = false
    private var requestInstalmentAmountTouched = false
    private var requestInstalmentVisibilityTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        setSupportActionBar(findViewById(R.id.request_toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val gson = Gson()

        val tempProfile = intent.getStringExtra("tempProfile")
        if (tempProfile != null && tempProfile != "") {
            intentActionGlobal = "newProfile"
        }

        val requestNameErrorText = findViewById<TextView>(R.id.request_name_error_tv)
        requestNameErrorText.visibility = View.GONE
        val requestIDNumberErrorText = findViewById<TextView>(R.id.request_id_number_error_tv)
        requestIDNumberErrorText.visibility = View.GONE
        val requestDebitOrderDayErrorText = findViewById<TextView>(R.id.request_debit_order_day_error_tv)
        requestDebitOrderDayErrorText.visibility = View.GONE

        var instalmentAmountChecked = false
        var maxInstalmentAmountChecked = false
        var instalmentVisibilitySelected = ""

        checkFilled()

        var transactionTypeSpinnerValue = Dropdown("", 0)
        var invokingMethodSpinnerValue = Dropdown("", 0)

        var transactionTypeDropdown = findViewById<Spinner>(R.id.btnTransactionType)
        var transactionTypeAdapter = CustomSpinnerAdapter(this, transactionTypeList)
        transactionTypeDropdown?.adapter = transactionTypeAdapter;
        transactionTypeDropdown.setSelection(transactionTypeList.size.minus(1))

        var invokingMethodDropdown = findViewById<Spinner>(R.id.btnInvokingMethod)
        var invokingMethodAdapter = CustomSpinnerAdapter(this, invokingMethodList)
        invokingMethodDropdown?.adapter = invokingMethodAdapter;
        invokingMethodDropdown.setSelection(invokingMethodList.size.minus(1))

        var requestNameEditText = findViewById<EditText>(R.id.request_name_et)
        var requestVerticalLine = findViewById<View>(R.id.divider_1)
        var requestGenPrefilledDetails = findViewById<CheckBox>(R.id.request_prefill_checkbox)
        var requestAmountEditText = findViewById<EditText>(R.id.request_amount_et)
        var requestPaymentReferenceEditText = findViewById<EditText>(R.id.request_reference_et)
        var requestAccountNumberEditText = findViewById<EditText>(R.id.request_acc_number_et)
        var requestIDNumberEditText = findViewById<EditText>(R.id.request_id_number_et)
        var requestMaxCollectionAmountEditText = findViewById<EditText>(R.id.request_max_collection_et)
        var requestDebitOrderDayEditText = findViewById<EditText>(R.id.request_debit_order_day_et)
        var requestCreditorEditText = findViewById<EditText>(R.id.request_creditor_et)
        var requestContractReferenceEditText = findViewById<EditText>(R.id.request_contract_reference_et)
        var requestInstalmentAmountEditText = findViewById<EditText>(R.id.request_instalment_amount_et)

        var requestInstalmentVisibilityLayout = findViewById<ConstraintLayout>(R.id.instalmentVisibility_layout)

        var requestInstalmentAmountRadioButton = findViewById<RadioButton>(R.id.rbnInstalmentAmount)
        var requestMaxInstalmentAmountRadioButton = findViewById<RadioButton>(R.id.rbnMaxInstalmentAmount)

        requestNameEditText.visibility = View.GONE
        requestVerticalLine.visibility = View.GONE
        requestGenPrefilledDetails.visibility = View.GONE
        requestAmountEditText.visibility = View.GONE
        requestPaymentReferenceEditText.visibility = View.GONE
        requestAccountNumberEditText.visibility = View.GONE
        requestIDNumberEditText.visibility = View.GONE
        requestMaxCollectionAmountEditText.visibility = View.GONE
        requestDebitOrderDayEditText.visibility = View.GONE
        requestCreditorEditText.visibility = View.GONE
        requestContractReferenceEditText.visibility = View.GONE
        requestInstalmentAmountEditText.visibility = View.GONE

        requestInstalmentVisibilityLayout.visibility = View.GONE

        requestNameEditText.doOnTextChanged { text, start, before, count ->
            requestNameTouched = true
            checkFilled()
        }

        requestAmountEditText.doOnTextChanged { text, start, before, count ->
            requestAmountTouched = true
            checkFilled()
        }
        requestPaymentReferenceEditText.doOnTextChanged { text, start, before, count ->
            requestPaymentReferenceTouched = true
            checkFilled()
        }

        requestAccountNumberEditText.doOnTextChanged { text, start, before, count ->
            requestAccountNumberTouched = true
            checkFilled()
        }
        requestIDNumberEditText.doOnTextChanged { text, start, before, count ->
            requestIDNumberTouched = true
            checkFilled()
        }
        requestMaxCollectionAmountEditText.doOnTextChanged { text, start, before, count ->
            requestMaxCollectionAmountTouched = true
            checkFilled()
        }
        requestDebitOrderDayEditText.doOnTextChanged { text, start, before, count ->
            requestDebitOrderDayTouched = true
            checkFilled()
        }
        requestCreditorEditText.doOnTextChanged { text, start, before, count ->
            requestCreditorTouched = true
            checkFilled()
        }
        requestContractReferenceEditText.doOnTextChanged { text, start, before, count ->
            requestContractReferenceTouched = true
            checkFilled()
        }
        requestInstalmentAmountEditText.doOnTextChanged { text, start, before, count ->
            requestInstalmentAmountTouched = true
            checkFilled()
        }

        requestInstalmentAmountRadioButton.setOnClickListener {
            requestInstalmentVisibilityTouched = true

            instalmentAmountChecked = !instalmentAmountChecked
            requestInstalmentAmountRadioButton.isChecked = instalmentAmountChecked

            instalmentVisibilitySelected = if (maxInstalmentAmountChecked) {
                "both"
            } else {
                "instalmentOnly"
            }

            checkFilled()
        }

        requestMaxInstalmentAmountRadioButton.setOnClickListener {
            requestInstalmentVisibilityTouched = true

            maxInstalmentAmountChecked = !maxInstalmentAmountChecked
            requestMaxInstalmentAmountRadioButton.isChecked = maxInstalmentAmountChecked

            instalmentVisibilitySelected = if (instalmentAmountChecked) {
                "both"
            } else {
                "maximumOnly"
            }

            checkFilled()
        }

        //Get value of selected spinner item
        transactionTypeDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, v: View?,
                postion: Int, arg3: Long
            ) {
                transactionTypeSpinnerValue = transactionTypeList[parent.getItemAtPosition(postion) as Int]

                checkFilled()

                if(invokingMethodSpinnerValue.name != "Invoking Method") {
                    if(transactionTypeSpinnerValue.name == "Card Payments") {
                        requestNameEditText.visibility = View.VISIBLE

                        requestVerticalLine.visibility = View.VISIBLE

                        requestGenPrefilledDetails.visibility = View.VISIBLE

                        requestAmountEditText.visibility = View.VISIBLE
                        requestPaymentReferenceEditText.visibility = View.VISIBLE

                        requestAccountNumberEditText.visibility = View.GONE
                        requestIDNumberEditText.visibility = View.GONE
                        requestMaxCollectionAmountEditText.visibility = View.GONE
                        requestDebitOrderDayEditText.visibility = View.GONE
                        requestCreditorEditText.visibility = View.GONE
                        requestContractReferenceEditText.visibility = View.GONE
                        requestInstalmentAmountEditText.visibility = View.GONE

                        requestInstalmentVisibilityLayout.visibility = View.GONE
                    } else if (transactionTypeSpinnerValue.name == "Debicheck/TT3") {
                        requestNameEditText.visibility = View.VISIBLE

                        requestVerticalLine.visibility = View.VISIBLE

                        requestGenPrefilledDetails.visibility = View.GONE

                        requestAmountEditText.visibility = View.GONE
                        requestPaymentReferenceEditText.visibility = View.GONE

                        requestAccountNumberEditText.visibility = View.VISIBLE
                        requestIDNumberEditText.visibility = View.VISIBLE
                        requestMaxCollectionAmountEditText.visibility = View.VISIBLE
                        requestDebitOrderDayEditText.visibility = View.VISIBLE
                        requestCreditorEditText.visibility = View.VISIBLE
                        requestContractReferenceEditText.visibility = View.VISIBLE
                        requestInstalmentAmountEditText.visibility = View.VISIBLE

                        requestInstalmentVisibilityLayout.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        invokingMethodDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, v: View?,
                postion: Int, arg3: Long
            ) {
                invokingMethodSpinnerValue = invokingMethodList[parent.getItemAtPosition(postion) as Int]

                if(transactionTypeSpinnerValue.name == "Card Payments") {
                    requestNameEditText.visibility = View.VISIBLE

                    requestVerticalLine.visibility = View.VISIBLE

                    requestGenPrefilledDetails.visibility = View.VISIBLE

                    requestAmountEditText.visibility = View.VISIBLE
                    requestPaymentReferenceEditText.visibility = View.VISIBLE

                    requestAccountNumberEditText.visibility = View.GONE
                    requestIDNumberEditText.visibility = View.GONE
                    requestMaxCollectionAmountEditText.visibility = View.GONE
                    requestDebitOrderDayEditText.visibility = View.GONE
                    requestCreditorEditText.visibility = View.GONE
                    requestContractReferenceEditText.visibility = View.GONE
                    requestInstalmentAmountEditText.visibility = View.GONE

                    requestInstalmentVisibilityLayout.visibility = View.GONE
                } else if (transactionTypeSpinnerValue.name == "Debicheck/TT3") {
                    requestNameEditText.visibility = View.VISIBLE

                    requestVerticalLine.visibility = View.VISIBLE

                    requestGenPrefilledDetails.visibility = View.GONE

                    requestAmountEditText.visibility = View.GONE
                    requestPaymentReferenceEditText.visibility = View.GONE

                    requestAccountNumberEditText.visibility = View.VISIBLE
                    requestIDNumberEditText.visibility = View.VISIBLE
                    requestMaxCollectionAmountEditText.visibility = View.VISIBLE
                    requestDebitOrderDayEditText.visibility = View.VISIBLE
                    requestCreditorEditText.visibility = View.VISIBLE
                    requestContractReferenceEditText.visibility = View.VISIBLE
                    requestInstalmentAmountEditText.visibility = View.VISIBLE

                    requestInstalmentVisibilityLayout.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        requestGenPrefilledDetails.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                val randomAmount = Random.nextDouble(1.00, 10.00)
                val df = DecimalFormat("#.##")
                var roundoff = df.format(randomAmount).replace(',', '.')

                val source = resources.getString(R.string.random_reference_source)
                val randomReference = java.util.Random().ints(5, 0, source.length)
                    .asSequence()
                    .map(source::get)
                    .joinToString("")

                requestAmountEditText.setText(roundoff)
                requestPaymentReferenceEditText.setText(randomReference)
                requestGenPrefilledDetails.buttonTintList = ColorStateList.valueOf(Color.parseColor("#7431D7"))
            } else {
                requestAmountEditText.text.clear()
                requestPaymentReferenceEditText.text.clear()
                requestGenPrefilledDetails.buttonTintList = ColorStateList.valueOf(Color.parseColor("#EAEAEA"))
            }
        }

        //Buttons
        val btnSkip = findViewById<Button>(R.id.skip_btn)
        btnSkip.setOnClickListener {
            val intent = Intent(this, ProfileCompletedActivity::class.java)
            intent.putExtra("tempRequest", "")
            intent.putExtra("tempProfile", tempProfile)
            startActivity(intent)
        }

        val btnNext = findViewById<Button>(R.id.next_btn)
        btnNext.setOnClickListener {
            val tempRequest = ApiRequest(
                transactionTypeSpinnerValue.name,
                invokingMethodSpinnerValue.name,
                requestNameEditText.text.trim().toString(),
                requestAmountEditText.text.trim().toString().toDoubleOrNull(),
                requestPaymentReferenceEditText.text.trim().toString(),
                requestAccountNumberEditText.text.trim().toString(),
                requestIDNumberEditText.text.trim().toString(),
                requestMaxCollectionAmountEditText.text.trim().toString(),
                requestDebitOrderDayEditText.text.trim().toString().toIntOrNull(),
                requestCreditorEditText.text.trim().toString(),
                requestContractReferenceEditText.text.trim().toString(),
                requestInstalmentAmountEditText.text.trim().toString().toDoubleOrNull(),
                instalmentVisibilitySelected
            )

            val tempRequestGson = gson.toJson(tempRequest)

            val intent = Intent(this, ProfileCompletedActivity::class.java)
            intent.putExtra("tempRequest", tempRequestGson)
            intent.putExtra("tempProfile", tempProfile)
            startActivity(intent)
        }

        val intentAction = intent.getStringExtra("Action")
        if (intentAction != null) {
            intentActionGlobal = intentAction
        }

        val btnSave = findViewById<Button>(R.id.save_btn)
        btnSave.visibility = View.GONE
        btnSave.setOnClickListener {
            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            var activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)

            val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
            var profiles = mutableListOf<Profile>()
            if (jsonProfile != null && jsonProfile != ""){
                val sType = object : TypeToken<List<Profile>>() { }.type
                profiles = gson.fromJson(jsonProfile, sType)
            }

            var apiRequests = mutableListOf<ApiRequest>()
            if (activeProfile.apiRequests != null) {
                apiRequests = activeProfile.apiRequests!!
            }

            if(intentAction == "Add"){
                val apiRequestToAdd = ApiRequest(
                    transactionTypeSpinnerValue.name,
                    invokingMethodSpinnerValue.name,
                    requestNameEditText.text.trim().toString(),
                    requestAmountEditText.text.trim().toString().toDoubleOrNull(),
                    requestPaymentReferenceEditText.text.trim().toString(),
                    requestAccountNumberEditText.text.trim().toString(),
                    requestIDNumberEditText.text.trim().toString(),
                    requestMaxCollectionAmountEditText.text.trim().toString(),
                    requestDebitOrderDayEditText.text.trim().toString().toIntOrNull(),
                    requestCreditorEditText.text.trim().toString(),
                    requestContractReferenceEditText.text.trim().toString(),
                    requestInstalmentAmountEditText.text.trim().toString().toDoubleOrNull(),
                    instalmentVisibilitySelected
                )

                apiRequests.add(apiRequestToAdd)

                for (profile in profiles) {
                    if (profile.name == activeProfile.name) {
                        profile.apiRequests = apiRequests

                        activeProfile = profile
                        profiles[profiles.indexOf(profile)] = activeProfile
                    }
                }

                val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()

                prefsEditor.putString("Profiles", gson.toJson(profiles))
                prefsEditor.putString("ActiveProfile", gson.toJson(activeProfile))
                prefsEditor.commit()
                prefsEditor.apply()

                finish()
            } else if (intentAction == "Edit") {
                val intentRequest = intent.getStringExtra("Request")

                var requestToEdit: ApiRequest = ApiRequest("", "", "", 0.00, "", "", "", "", 0, "", "", 0.00, "")
                var requests = mutableListOf<ApiRequest>()
                requests = activeProfile.apiRequests!!

                for (request in requests) {
                    if (request.name == intentRequest) {
                        requestToEdit = request
                    }
                }

                var requestName = requestToEdit.name

                requestToEdit.name = findViewById<EditText>(R.id.request_name_et).text.toString()

                val invokingMethod = findViewById<View>(R.id.btnInvokingMethod) as Spinner
                val invokingMethodSpinnerPosition = invokingMethod.selectedItem
                if (invokingMethodSpinnerPosition != null){
                    val spinnerValue = invokingMethodList[invokingMethodSpinnerPosition as Int]
                    if (requestToEdit.invokingMethod != spinnerValue.name) {
                        requestToEdit.invokingMethod = spinnerValue.name
                    }
                }

                val transactionType = findViewById<View>(R.id.btnTransactionType) as Spinner
                val transactionTypeSpinnerPosition = transactionType.selectedItem
                if (transactionTypeSpinnerPosition != null){
                    val spinnerValue = transactionTypeList[transactionTypeSpinnerPosition as Int]
                    if (requestToEdit.transactionType != spinnerValue.name) {
                        requestToEdit.transactionType = spinnerValue.name
                    }

                    if (spinnerValue.name == "Card Payments") {
                        requestToEdit.amount = findViewById<EditText>(R.id.request_amount_et).text.toString().toDoubleOrNull()
                        requestToEdit.paymentReference = findViewById<EditText>(R.id.request_reference_et).text.toString()
                    } else if (spinnerValue.name == "Debicheck/TT3") {
                        requestToEdit.accountNumber = findViewById<EditText>(R.id.request_acc_number_et).text.toString()
                        requestToEdit.idNumber = findViewById<EditText>(R.id.request_id_number_et).text.toString()
                        requestToEdit.maxCollectionAmount = findViewById<EditText>(R.id.request_max_collection_et).text.toString()
                        requestToEdit.debitOrderDay = findViewById<EditText>(R.id.request_debit_order_day_et).text.toString().toIntOrNull()
                        requestToEdit.creditor = findViewById<EditText>(R.id.request_creditor_et).text.toString()
                        requestToEdit.contractReference = findViewById<EditText>(R.id.request_contract_reference_et).text.toString()
                        requestToEdit.instalmentAmount = findViewById<EditText>(R.id.request_instalment_amount_et).text.toString().toDoubleOrNull()

                        if (instalmentAmountChecked && maxInstalmentAmountChecked) {
                            requestToEdit.instalmentVisibility = "both"
                        } else if (instalmentAmountChecked && !maxInstalmentAmountChecked) {
                            requestToEdit.instalmentVisibility = "instalmentOnly"
                        } else if (!instalmentAmountChecked && maxInstalmentAmountChecked) {
                            requestToEdit.instalmentVisibility = "maximumOnly"
                        }
                    }
                }

                for (request in requests) {
                    if (request.name == intentRequest) {
                        requests[requests.indexOf(request)] = requestToEdit
                    }
                }

                activeProfile.apiRequests = requests

                for (profile in profiles) {
                    if (profile.name == activeProfile.name) {
                        profiles[profiles.indexOf(profile)] = activeProfile
                    }
                }

                val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()

                prefsEditor.putString("Profiles", gson.toJson(profiles))
                prefsEditor.putString("ActiveProfile", gson.toJson(activeProfile))
                prefsEditor.commit()
                prefsEditor.apply()

                finish()
            }
        }

        val btnCancel = findViewById<Button>(R.id.cancel_btn)
        btnCancel.setOnClickListener {
            if (intentActionGlobal == "Add" || intentActionGlobal == "Edit") {
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }

        if(intentAction == "Add") {
            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            val profileValues: Profile = gson.fromJson(jsonActiveProfile, Profile::class.java)

            val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
            toolbarTitle.text = profileValues.name

            btnSkip.visibility = View.GONE
            btnNext.visibility = View.GONE
            btnSave.visibility = View.VISIBLE

            findViewById<ImageView>(R.id.progress_bar_1_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_2_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_3_iv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_1_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_2_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_3_tv).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_1).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_2).visibility = View.GONE
        } else if (intentAction == "Edit") {
            //Get request to edit, update fields, and save any updates
            val intentRequest = intent.getStringExtra("Request")
            if (intentRequest != null) {
                requestToEditName = intentRequest
            }

            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            val profileValues: Profile = gson.fromJson(jsonActiveProfile, Profile::class.java)

            val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
            toolbarTitle.text = profileValues.name

            btnSkip.visibility = View.GONE
            btnNext.visibility = View.GONE
            btnSave.visibility = View.VISIBLE

            findViewById<ImageView>(R.id.progress_bar_1_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_2_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_3_iv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_1_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_2_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_3_tv).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_1).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_2).visibility = View.GONE

            var requestToEdit: ApiRequest = ApiRequest("", "", "", 0.00, "", "", "", "", 0, "", "", 0.00, "")
            var requests = mutableListOf<ApiRequest>()
            requests = profileValues.apiRequests!!

            for (request in requests) {
                if (request.name == intentRequest) {
                    requestToEdit = request
                }
            }

            if (requestToEdit.transactionType != "") {
                val ttToIndex = Dropdown(requestToEdit.transactionType, R.drawable.ic_file_logo)

                val indexTransactionType = transactionTypeList.indexOfFirst{ttToIndex.name == it.name}
                transactionTypeDropdown.setSelection(indexTransactionType)
            }

            if (requestToEdit.invokingMethod != "") {
                val imToIndex = Dropdown(requestToEdit.invokingMethod, R.drawable.ic_link)

                val indexInvokingMethod = invokingMethodList.indexOfFirst{imToIndex.name == it.name}
                invokingMethodDropdown.setSelection(indexInvokingMethod)
            }

            findViewById<EditText>(R.id.request_name_et).setText(requestToEdit.name)

            if (requestToEdit.transactionType == "Card Payments") {
                requestNameEditText.visibility = View.VISIBLE

                requestVerticalLine.visibility = View.VISIBLE

                requestGenPrefilledDetails.visibility = View.VISIBLE

                requestAmountEditText.visibility = View.VISIBLE
                requestPaymentReferenceEditText.visibility = View.VISIBLE

                requestAccountNumberEditText.visibility = View.GONE
                requestIDNumberEditText.visibility = View.GONE
                requestMaxCollectionAmountEditText.visibility = View.GONE
                requestDebitOrderDayEditText.visibility = View.GONE
                requestCreditorEditText.visibility = View.GONE
                requestContractReferenceEditText.visibility = View.GONE
                requestInstalmentAmountEditText.visibility = View.GONE

                findViewById<EditText>(R.id.request_amount_et).setText(requestToEdit.amount.toString())
                findViewById<EditText>(R.id.request_reference_et).setText(requestToEdit.paymentReference)
            } else if (requestToEdit.transactionType == "Debicheck/TT3") {
                requestNameEditText.visibility = View.VISIBLE

                requestVerticalLine.visibility = View.VISIBLE

                requestGenPrefilledDetails.visibility = View.GONE

                requestAmountEditText.visibility = View.GONE
                requestPaymentReferenceEditText.visibility = View.GONE

                requestAccountNumberEditText.visibility = View.VISIBLE
                requestIDNumberEditText.visibility = View.VISIBLE
                requestMaxCollectionAmountEditText.visibility = View.VISIBLE
                requestDebitOrderDayEditText.visibility = View.VISIBLE
                requestCreditorEditText.visibility = View.VISIBLE
                requestContractReferenceEditText.visibility = View.VISIBLE
                requestInstalmentAmountEditText.visibility = View.VISIBLE

                findViewById<EditText>(R.id.request_acc_number_et).setText(requestToEdit.accountNumber)
                findViewById<EditText>(R.id.request_id_number_et).setText(requestToEdit.idNumber)
                findViewById<EditText>(R.id.request_max_collection_et).setText(requestToEdit.maxCollectionAmount)
                findViewById<EditText>(R.id.request_debit_order_day_et).setText(requestToEdit.debitOrderDay.toString())
                findViewById<EditText>(R.id.request_creditor_et).setText(requestToEdit.creditor)
                findViewById<EditText>(R.id.request_contract_reference_et).setText(requestToEdit.contractReference)
                findViewById<EditText>(R.id.request_instalment_amount_et).setText(requestToEdit.instalmentAmount.toString())

                if (requestToEdit.instalmentVisibility != "" && requestToEdit.instalmentVisibility != null) {
                    when (requestToEdit.instalmentVisibility) {
                        "both" -> {
                            instalmentAmountChecked = true
                            maxInstalmentAmountChecked = true
                            requestInstalmentAmountRadioButton.isChecked = true
                            requestMaxInstalmentAmountRadioButton.isChecked = true
                        }
                        "instalmentOnly" -> {
                            instalmentAmountChecked = true
                            maxInstalmentAmountChecked = false
                            requestInstalmentAmountRadioButton.isChecked = true
                            requestMaxInstalmentAmountRadioButton.isChecked = false
                        }
                        "maximumOnly" -> {
                            instalmentAmountChecked = false
                            maxInstalmentAmountChecked = true
                            requestInstalmentAmountRadioButton.isChecked = false
                            requestMaxInstalmentAmountRadioButton.isChecked = true
                        }
                    }
                }
            }
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

    private fun checkFilled() {
        val nextButton = findViewById<Button>(R.id.next_btn)
        val saveButton = findViewById<Button>(R.id.save_btn)
        val filled = isFilled()

        nextButton.isEnabled = filled
        saveButton.isEnabled = filled

        if (intentActionGlobal == "Edit" || intentActionGlobal == "Add") {
            if (filled) {
                saveButton.setBackgroundResource(R.drawable.gradient_button)
            } else {
                saveButton.setBackgroundResource(R.drawable.disabled_button)
            }
        } else {
            if (filled) {
                nextButton.setBackgroundResource(R.drawable.gradient_button)
            } else {
                nextButton.setBackgroundResource(R.drawable.disabled_button)
            }
        }
    }

    private fun isFilled(): Boolean {
        val sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val gson = Gson()
        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
        }

        var filled = true

        val requestNameEditText = findViewById<EditText>(R.id.request_name_et).text.trim()
        val requestGenPrefilledDetails = findViewById<CheckBox>(R.id.request_prefill_checkbox).text.trim()
        val requestAmountEditText = findViewById<EditText>(R.id.request_amount_et).text.trim()
        val requestPaymentReferenceEditText = findViewById<EditText>(R.id.request_reference_et).text.trim()
        val requestAccountNumberEditText = findViewById<EditText>(R.id.request_acc_number_et).text.trim()
        val requestIDNumberEditText = findViewById<EditText>(R.id.request_id_number_et).text.trim()
        val requestMaxCollectionAmountEditText = findViewById<EditText>(R.id.request_max_collection_et).text.trim()
        val requestDebitOrderDayEditText = findViewById<EditText>(R.id.request_debit_order_day_et).text.trim()
        val requestCreditorEditText = findViewById<EditText>(R.id.request_creditor_et).text.trim()
        val requestContractReferenceEditText = findViewById<EditText>(R.id.request_contract_reference_et).text.trim()
        val requestInstalmentAmountEditText = findViewById<EditText>(R.id.request_instalment_amount_et).text.trim()
        var requestInstalmentAmountRadioButton = findViewById<RadioButton>(R.id.rbnInstalmentAmount)
        var requestMaxInstalmentAmountRadioButton = findViewById<RadioButton>(R.id.rbnMaxInstalmentAmount)

        val transactionType = findViewById<View>(R.id.btnTransactionType) as Spinner
        val spinnerPosition = transactionType.selectedItem
        if (spinnerPosition != null){
            val spinnerValue = transactionTypeList[spinnerPosition as Int]

            if (TextUtils.isEmpty(requestNameEditText)) {
                if (requestNameTouched) {
                    findViewById<EditText>(R.id.request_name_et).setBackgroundResource(R.drawable.edit_text_border_red)
                }
                filled = false
            } else {
                var uniqueName = true
                if (intentActionGlobal != "newProfile") {
                    if(jsonActiveProfile != ""){
                        if(activeProfile.apiRequests?.isNotEmpty() == true) {
                            for (request in activeProfile.apiRequests!!) {
                                if (request.name.equals(requestNameEditText.toString(), true) && (requestToEditName != request.name)) {
                                    uniqueName = false
                                }
                            }
                        }
                    }
                }
                if (uniqueName) {
                    findViewById<EditText>(R.id.request_name_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                    findViewById<TextView>(R.id.request_name_error_tv).visibility = View.GONE
                } else {
                    findViewById<EditText>(R.id.request_name_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    findViewById<TextView>(R.id.request_name_error_tv).text = resources.getString(R.string.request_name_error)
                    findViewById<TextView>(R.id.request_name_error_tv).visibility = View.VISIBLE
                    filled = false
                }
            }

            if (spinnerValue.name == "Card Payments") {
                if (TextUtils.isEmpty(requestAmountEditText)) {
                    if (requestAmountTouched) {
                        findViewById<EditText>(R.id.request_amount_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_amount_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }

                if (TextUtils.isEmpty(requestPaymentReferenceEditText)) {
                    if (requestPaymentReferenceTouched) {
                        findViewById<EditText>(R.id.request_reference_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_reference_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
            } else if (spinnerValue.name == "Debicheck/TT3") {
                if (TextUtils.isEmpty(requestAccountNumberEditText)) {
                    if (requestAccountNumberTouched) {
                        findViewById<EditText>(R.id.request_acc_number_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_acc_number_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
                if (TextUtils.isEmpty(requestIDNumberEditText)) {
                    if (requestIDNumberTouched) {
                        findViewById<EditText>(R.id.request_id_number_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    if (requestIDNumberEditText.length != 13) {
                        findViewById<EditText>(R.id.request_id_number_et).setBackgroundResource(R.drawable.edit_text_border_red)
                        findViewById<TextView>(R.id.request_id_number_error_tv).text = resources.getString(R.string.id_number_error_13_digits)
                        findViewById<TextView>(R.id.request_id_number_error_tv).visibility = View.VISIBLE
                        filled = false
                    } else {
                        findViewById<EditText>(R.id.request_id_number_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                        findViewById<TextView>(R.id.request_id_number_error_tv).visibility = View.GONE
                    }
                }
                if (TextUtils.isEmpty(requestMaxCollectionAmountEditText)) {
                    if (requestMaxCollectionAmountTouched) {
                        findViewById<EditText>(R.id.request_max_collection_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_max_collection_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
                if (TextUtils.isEmpty(requestDebitOrderDayEditText)) {
                    if (requestDebitOrderDayTouched) {
                        findViewById<EditText>(R.id.request_debit_order_day_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    if (requestDebitOrderDayEditText.toString().toInt() > 31 || requestDebitOrderDayEditText.toString().toInt() < 1) {
                        findViewById<EditText>(R.id.request_debit_order_day_et).setBackgroundResource(R.drawable.edit_text_border_red)
                        findViewById<TextView>(R.id.request_debit_order_day_error_tv).text = resources.getString(R.string.debit_order_day_error_date)
                        findViewById<TextView>(R.id.request_debit_order_day_error_tv).visibility = View.VISIBLE
                        filled = false
                    } else {
                        findViewById<EditText>(R.id.request_debit_order_day_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                        findViewById<TextView>(R.id.request_debit_order_day_error_tv).visibility = View.GONE
                    }
                }
                if (TextUtils.isEmpty(requestCreditorEditText)) {
                    if (requestCreditorTouched) {
                        findViewById<EditText>(R.id.request_creditor_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_creditor_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
                if (TextUtils.isEmpty(requestContractReferenceEditText)) {
                    if (requestContractReferenceTouched) {
                        findViewById<EditText>(R.id.request_contract_reference_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_contract_reference_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
                if (TextUtils.isEmpty(requestInstalmentAmountEditText)) {
                    if (requestInstalmentAmountTouched) {
                        findViewById<EditText>(R.id.request_instalment_amount_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<EditText>(R.id.request_instalment_amount_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
                if (!requestInstalmentAmountRadioButton.isChecked && !requestMaxInstalmentAmountRadioButton.isChecked) {
                    if (requestInstalmentVisibilityTouched) {
                        findViewById<ConstraintLayout>(R.id.instalmentVisibility_layout).setBackgroundResource(R.drawable.edit_text_border_red)
                    }
                    filled = false
                } else {
                    findViewById<ConstraintLayout>(R.id.instalmentVisibility_layout).setBackgroundResource(R.drawable.edit_text_border_grey)
                }
            }
        }

        return filled
    }
}