package za.co.synthesis.halo.halo_dot_go_enabler

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TransactionDetailsActivity : AppCompatActivity() {

    private val api: Api by lazy { Api(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)

        val gson = Gson()
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile = Profile("", "", "", "", "", "", "", null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
        }

        //Get TextViews and Buttons
        val idTextView = findViewById<TextView>(R.id.transaction_details_id_tv)
        val idTextViewValue = findViewById<TextView>(R.id.transaction_details_id_value_tv)
        val passthroughUserIdTextView = findViewById<TextView>(R.id.transaction_details_passthroughUserId_tv)
        val passthroughUserIdTextViewValue = findViewById<TextView>(R.id.transaction_details_passthroughUserId_value_tv)
        val merchantTransactionReferenceTextView = findViewById<TextView>(R.id.transaction_details_merchantTransactionReference_tv)
        val merchantTransactionReferenceTextViewValue = findViewById<TextView>(R.id.transaction_details_merchantTransactionReference_value_tv)
        val encryptedV2 = findViewById<TextView>(R.id.transaction_details_encryptedV2_tv)
        val jweTextView = findViewById<TextView>(R.id.transaction_details_jwe_tv)
        val jweTextViewValue = findViewById<TextView>(R.id.transaction_details_jwe_value_tv)
        val keyVersionTextView = findViewById<TextView>(R.id.transaction_details_keyVersion_tv)
        val keyVersionTextViewValue = findViewById<TextView>(R.id.transaction_details_keyVersion_value_tv)
        val fingerprintTextView = findViewById<TextView>(R.id.transaction_details_fingerprint_tv)
        val fingerprintTextViewValue = findViewById<TextView>(R.id.transaction_details_fingerprint_value_tv)

        val seeMoreButton1 = findViewById<Button>(R.id.see_more_1_btn)

        val latitudeTextView = findViewById<TextView>(R.id.transaction_details_latitude_tv)
        val latitudeTextViewValue = findViewById<TextView>(R.id.transaction_details_latitude_value_tv)
        val longitudeTextView = findViewById<TextView>(R.id.transaction_details_longitude_tv)
        val longitudeTextViewValue = findViewById<TextView>(R.id.transaction_details_longitude_value_tv)
        val deviceInstallationIdTextView = findViewById<TextView>(R.id.transaction_details_deviceinstallationId_tv)
        val deviceInstallationIdTextViewValue = findViewById<TextView>(R.id.transaction_details_deviceinstallationId_value_tv)
        val transactionstatusIdTextView = findViewById<TextView>(R.id.transaction_details_transactionstatusId_tv)
        val transactionstatusIdTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionstatusId_value_tv)
        val transactionDispositionIdTextView = findViewById<TextView>(R.id.transaction_details_transactionDispositionId_tv)
        val transactionDispositionIdTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionDispositionId_value_tv)
        val amountTextView = findViewById<TextView>(R.id.transaction_details_amount_tv)
        val amountTextViewValue = findViewById<TextView>(R.id.transaction_details_amount_value_tv)
        val accountNumberTextView = findViewById<TextView>(R.id.transaction_details_accountNumber_tv)
        val accountNumberTextViewValue = findViewById<TextView>(R.id.transaction_details_accountNumber_value_tv)
        val currencyTextView = findViewById<TextView>(R.id.transaction_details_currency_tv)
        val currencyTextViewValue = findViewById<TextView>(R.id.transaction_details_currency_value_tv)
        val responseCodeTextView = findViewById<TextView>(R.id.transaction_details_responseCode_tv)
        val responseCodeTextViewValue = findViewById<TextView>(R.id.transaction_details_responseCode_value_tv)
        val transactionTypeIdTextView = findViewById<TextView>(R.id.transaction_details_transactionTypeId_tv)
        val transactionTypeIdTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionTypeId_value_tv)
        val originalTransactionIdTextView = findViewById<TextView>(R.id.transaction_details_originalTransactionId_tv)
        val originalTransactionIdTextViewValue = findViewById<TextView>(R.id.transaction_details_originalTransactionId_value_tv)
        val acquirerIdTextView = findViewById<TextView>(R.id.transaction_details_acquirerId_tv)
        val acquirerIdTextViewValue = findViewById<TextView>(R.id.transaction_details_acquirerId_value_tv)
        val userIdTextView = findViewById<TextView>(R.id.transaction_details_userId_tv)
        val userIdTextViewValue = findViewById<TextView>(R.id.transaction_details_userId_value_tv)
        val transactionFeesConfigIdTextView = findViewById<TextView>(R.id.transaction_details_transactionFeesConfigId_tv)
        val transactionFeesConfigIdTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionFeesConfigId_value_tv)
        val TransactionFeeTextView = findViewById<TextView>(R.id.transaction_details_TransactionFee_tv)
        val TransactionFeeTextViewValue = findViewById<TextView>(R.id.transaction_details_TransactionFee_value_tv)
        val authorisationCodeTextView = findViewById<TextView>(R.id.transaction_details_authorisationCode_tv)
        val authorisationCodeTextViewValue = findViewById<TextView>(R.id.transaction_details_authorisationCode_value_tv)

        val seeMoreButton2 = findViewById<Button>(R.id.see_more_2_btn)

        val createdAtTextView = findViewById<TextView>(R.id.transaction_details_createdAt_tv)
        val createdAtTextViewValue = findViewById<TextView>(R.id.transaction_details_createdAt_value_tv)
        val updatedAtTextView = findViewById<TextView>(R.id.transaction_details_updatedAt_tv)
        val updatedAtTextViewValue = findViewById<TextView>(R.id.transaction_details_updatedAt_value_tv)
        val effectiveDateTextView = findViewById<TextView>(R.id.transaction_details_effectiveDate_tv)
        val effectiveDateTextViewValue = findViewById<TextView>(R.id.transaction_details_effectiveDate_value_tv)
        val expiryDateTextView = findViewById<TextView>(R.id.transaction_details_expiryDate_tv)
        val expiryDateTextViewValue = findViewById<TextView>(R.id.transaction_details_expiryDate_value_tv)
        val tidTextView = findViewById<TextView>(R.id.transaction_details_tid_tv)
        val tidTextViewValue = findViewById<TextView>(R.id.transaction_details_tid_value_tv)
        val transactionReferenceTextView = findViewById<TextView>(R.id.transaction_details_transactionReference_tv)
        val transactionReferenceTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionReference_value_tv)
        val merchantNameTextView = findViewById<TextView>(R.id.transaction_details_merchantName_tv)
        val merchantNameTextViewValue = findViewById<TextView>(R.id.transaction_details_merchantName_value_tv)
        val formattedMerchantAddressTextView = findViewById<TextView>(R.id.transaction_details_formattedMerchantAddress_tv)
        val formattedMerchantAddressTextViewValue = findViewById<TextView>(R.id.transaction_details_formattedMerchantAddress_value_tv)
        val transactionTypeTextView = findViewById<TextView>(R.id.transaction_details_transactionType_tv)
        val transactionTypeTextViewValue = findViewById<TextView>(R.id.transaction_details_transactionType_value_tv)
        val approvalTextTextView = findViewById<TextView>(R.id.transaction_details_approvalText_tv)
        val approvalTextTextViewValue = findViewById<TextView>(R.id.transaction_details_approvalText_value_tv)
        val aidTextView = findViewById<TextView>(R.id.transaction_details_aid_tv)
        val aidTextViewValue = findViewById<TextView>(R.id.transaction_details_aid_value_tv)

        val seeMoreButton3 = findViewById<Button>(R.id.see_more_3_btn)

        val applicationLabelTextView = findViewById<TextView>(R.id.transaction_details_applicationLabel_tv)
        val applicationLabelTextViewValue = findViewById<TextView>(R.id.transaction_details_applicationLabel_value_tv)
        val applicationPreferredNameTextView = findViewById<TextView>(R.id.transaction_details_applicationPreferredName_tv)
        val applicationPreferredNameTextViewValue = findViewById<TextView>(R.id.transaction_details_applicationPreferredName_value_tv)
        val associationTextView = findViewById<TextView>(R.id.transaction_details_association_tv)
        val associationTextViewValue = findViewById<TextView>(R.id.transaction_details_association_value_tv)
        val isoResponseCodeTextView = findViewById<TextView>(R.id.transaction_details_isoResponseCode_tv)
        val isoResponseCodeTextViewValue = findViewById<TextView>(R.id.transaction_details_isoResponseCode_value_tv)
        val cryptogramTextView = findViewById<TextView>(R.id.transaction_details_cryptogram_tv)
        val cryptogramTextViewValue = findViewById<TextView>(R.id.transaction_details_cryptogram_value_tv)
        val cryptogramTypeTextView = findViewById<TextView>(R.id.transaction_details_cryptogramType_tv)
        val cryptogramTypeTextViewValue = findViewById<TextView>(R.id.transaction_details_cryptogramType_value_tv)
        val tvrTextView = findViewById<TextView>(R.id.transaction_details_tvr_tv)
        val tvrTextViewValue = findViewById<TextView>(R.id.transaction_details_tvr_value_tv)
        val dateTextView = findViewById<TextView>(R.id.transaction_details_date_tv)
        val dateTextViewValue = findViewById<TextView>(R.id.transaction_details_date_value_tv)
        val timeTextView = findViewById<TextView>(R.id.transaction_details_time_tv)
        val timeTextViewValue = findViewById<TextView>(R.id.transaction_details_time_value_tv)
        val maskedPanTextView = findViewById<TextView>(R.id.transaction_details_maskedPan_tv)
        val maskedPanTextViewValue = findViewById<TextView>(R.id.transaction_details_maskedPan_value_tv)

        idTextView.visibility = View.GONE
        idTextViewValue.visibility = View.GONE
        passthroughUserIdTextView.visibility = View.GONE
        passthroughUserIdTextViewValue.visibility = View.GONE
        merchantTransactionReferenceTextView.visibility = View.GONE
        merchantTransactionReferenceTextViewValue.visibility = View.GONE
        encryptedV2.visibility = View.GONE
        jweTextView.visibility = View.GONE
        jweTextViewValue.visibility = View.GONE
        keyVersionTextView.visibility = View.GONE
        keyVersionTextViewValue.visibility = View.GONE
        fingerprintTextView.visibility = View.GONE
        fingerprintTextViewValue.visibility = View.GONE
        seeMoreButton1.visibility = View.GONE
        latitudeTextView.visibility = View.GONE
        latitudeTextViewValue.visibility = View.GONE
        longitudeTextView.visibility = View.GONE
        longitudeTextViewValue.visibility = View.GONE
        deviceInstallationIdTextView.visibility = View.GONE
        deviceInstallationIdTextViewValue.visibility = View.GONE
        transactionstatusIdTextView.visibility = View.GONE
        transactionstatusIdTextViewValue.visibility = View.GONE
        transactionDispositionIdTextView.visibility = View.GONE
        transactionDispositionIdTextViewValue.visibility = View.GONE
        amountTextView.visibility = View.GONE
        amountTextViewValue.visibility = View.GONE
        accountNumberTextView.visibility = View.GONE
        accountNumberTextViewValue.visibility = View.GONE
        currencyTextView.visibility = View.GONE
        currencyTextViewValue.visibility = View.GONE
        responseCodeTextView.visibility = View.GONE
        responseCodeTextViewValue.visibility = View.GONE
        transactionTypeIdTextView.visibility = View.GONE
        transactionTypeIdTextViewValue.visibility = View.GONE
        originalTransactionIdTextView.visibility = View.GONE
        originalTransactionIdTextViewValue.visibility = View.GONE
        acquirerIdTextView.visibility = View.GONE
        acquirerIdTextViewValue.visibility = View.GONE
        userIdTextView.visibility = View.GONE
        userIdTextViewValue.visibility = View.GONE
        transactionFeesConfigIdTextView.visibility = View.GONE
        transactionFeesConfigIdTextViewValue.visibility = View.GONE
        TransactionFeeTextView.visibility = View.GONE
        TransactionFeeTextViewValue.visibility = View.GONE
        authorisationCodeTextView.visibility = View.GONE
        authorisationCodeTextViewValue.visibility = View.GONE
        seeMoreButton2.visibility = View.GONE
        createdAtTextView.visibility = View.GONE
        createdAtTextViewValue.visibility = View.GONE
        updatedAtTextView.visibility = View.GONE
        updatedAtTextViewValue.visibility = View.GONE
        effectiveDateTextView.visibility = View.GONE
        effectiveDateTextViewValue.visibility = View.GONE
        expiryDateTextView.visibility = View.GONE
        expiryDateTextViewValue.visibility = View.GONE
        tidTextView.visibility = View.GONE
        tidTextViewValue.visibility = View.GONE
        transactionReferenceTextView.visibility = View.GONE
        transactionReferenceTextViewValue.visibility = View.GONE
        merchantNameTextView.visibility = View.GONE
        merchantNameTextViewValue.visibility = View.GONE
        formattedMerchantAddressTextView.visibility = View.GONE
        formattedMerchantAddressTextViewValue.visibility = View.GONE
        transactionTypeTextView.visibility = View.GONE
        transactionTypeTextViewValue.visibility = View.GONE
        approvalTextTextView.visibility = View.GONE
        approvalTextTextViewValue.visibility = View.GONE
        aidTextView.visibility = View.GONE
        aidTextViewValue.visibility = View.GONE
        seeMoreButton3.visibility = View.GONE
        applicationLabelTextView.visibility = View.GONE
        applicationLabelTextViewValue.visibility = View.GONE
        applicationPreferredNameTextView.visibility = View.GONE
        applicationPreferredNameTextViewValue.visibility = View.GONE
        associationTextView.visibility = View.GONE
        associationTextViewValue.visibility = View.GONE
        isoResponseCodeTextView.visibility = View.GONE
        isoResponseCodeTextViewValue.visibility = View.GONE
        cryptogramTextView.visibility = View.GONE
        cryptogramTextViewValue.visibility = View.GONE
        cryptogramTypeTextView.visibility = View.GONE
        cryptogramTypeTextViewValue.visibility = View.GONE
        tvrTextView.visibility = View.GONE
        tvrTextViewValue.visibility = View.GONE
        dateTextView.visibility = View.GONE
        dateTextViewValue.visibility = View.GONE
        timeTextView.visibility = View.GONE
        timeTextViewValue.visibility = View.GONE
        maskedPanTextView.visibility = View.GONE
        maskedPanTextViewValue.visibility = View.GONE

        //TT3 Get TextViews and Buttons
        val tt3QrCodeStateTextView = findViewById<TextView>(R.id.tt3_transaction_details_qrCodeState_tv)
        val tt3QrCodeStateTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_qrCodeState_value_tv)
        val tt3TransactionIdTextView = findViewById<TextView>(R.id.tt3_transaction_details_transactionId_tv)
        val tt3TransactionIdTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_transactionId_value_tv)
        val tt3MerchantTransactionReferenceTextView = findViewById<TextView>(R.id.tt3_transaction_details_merchantTransactionReference_tv)
        val tt3MerchantTransactionReferenceTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_merchantTransactionReference_value_tv)
        val tt3UserIdTextView = findViewById<TextView>(R.id.tt3_transaction_details_userId_tv)
        val tt3UserIdTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_userId_value_tv)
        val tt3StatusTextView = findViewById<TextView>(R.id.tt3_transaction_details_status_tv)
        val tt3StatusTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_status_value_tv)
        val tt3DispositionTextView = findViewById<TextView>(R.id.tt3_transaction_details_disposition_tv)
        val tt3DispositionTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_disposition_value_tv)
        val tt3AmountTextView = findViewById<TextView>(R.id.tt3_transaction_details_amount_tv)
        val tt3AmountTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_amount_value_tv)
        val tt3CurrencyTextView = findViewById<TextView>(R.id.tt3_transaction_details_currency_tv)
        val tt3CurrencyTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_currency_value_tv)
        val tt3TypeTextView = findViewById<TextView>(R.id.tt3_transaction_details_type_tv)
        val tt3TypeTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_type_value_tv)
        val tt3ResponseCodeTextView = findViewById<TextView>(R.id.tt3_transaction_details_responseCode_tv)
        val tt3ResponseCodeTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_responseCode_value_tv)
        val tt3AuthorisationCodeTextView = findViewById<TextView>(R.id.tt3_transaction_details_authorisationCode_tv)
        val tt3AuthorisationCodeTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_authorisationCode_value_tv)

        val seeMoreButton4 = findViewById<Button>(R.id.see_more_4_btn)

        val tt3CreatedAtTextView = findViewById<TextView>(R.id.tt3_transaction_details_createdAt_tv)
        val tt3CreatedAtTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_createdAt_value_tv)
        val tt3UpdatedAtTextView = findViewById<TextView>(R.id.tt3_transaction_details_updatedAt_tv)
        val tt3UpdatedAtTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_updatedAt_value_tv)
        val tt3AccountNumberTextView = findViewById<TextView>(R.id.tt3_transaction_details_accountNumber_tv)
        val tt3AccountNumberTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_accountNumber_value_tv)
        val tt3IdNumberTextView = findViewById<TextView>(R.id.tt3_transaction_details_idNumber_tv)
        val tt3IdNumberTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_idNumber_value_tv)
        val tt3CreditorABSNTextView = findViewById<TextView>(R.id.tt3_transaction_details_creditorABSN_tv)
        val tt3CreditorABSNTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_creditorABSN_value_tv)
        val tt3MaxCollectionAmountTextView = findViewById<TextView>(R.id.tt3_transaction_details_maxCollectionAmount_tv)
        val tt3MaxCollectionAmountTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_maxCollectionAmount_value_tv)
        val tt3ContractReferenceTextView = findViewById<TextView>(R.id.tt3_transaction_details_contractReference_tv)
        val tt3ContractReferenceTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_contractReference_value_tv)
        val tt3CollectionDayTextView = findViewById<TextView>(R.id.tt3_transaction_details_collectionDay_tv)
        val tt3CollectionDayTextViewValue = findViewById<TextView>(R.id.tt3_transaction_details_collectionDay_value_tv)

        tt3QrCodeStateTextView.visibility = View.GONE
        tt3QrCodeStateTextViewValue.visibility = View.GONE
        tt3TransactionIdTextView.visibility = View.GONE
        tt3TransactionIdTextViewValue.visibility = View.GONE
        tt3MerchantTransactionReferenceTextView.visibility = View.GONE
        tt3MerchantTransactionReferenceTextViewValue.visibility = View.GONE
        tt3UserIdTextView.visibility = View.GONE
        tt3UserIdTextViewValue.visibility = View.GONE
        tt3StatusTextView.visibility = View.GONE
        tt3StatusTextViewValue.visibility = View.GONE
        tt3DispositionTextView.visibility = View.GONE
        tt3DispositionTextViewValue.visibility = View.GONE
        tt3AmountTextView.visibility = View.GONE
        tt3AmountTextViewValue.visibility = View.GONE
        tt3CurrencyTextView.visibility = View.GONE
        tt3CurrencyTextViewValue.visibility = View.GONE
        tt3TypeTextView.visibility = View.GONE
        tt3TypeTextViewValue.visibility = View.GONE
        tt3ResponseCodeTextView.visibility = View.GONE
        tt3ResponseCodeTextViewValue.visibility = View.GONE
        tt3AuthorisationCodeTextView.visibility = View.GONE
        tt3AuthorisationCodeTextViewValue.visibility = View.GONE

        seeMoreButton4.visibility = View.GONE

        tt3CreatedAtTextView.visibility = View.GONE
        tt3CreatedAtTextViewValue.visibility = View.GONE
        tt3UpdatedAtTextView.visibility = View.GONE
        tt3UpdatedAtTextViewValue.visibility = View.GONE
        tt3AccountNumberTextView.visibility = View.GONE
        tt3AccountNumberTextViewValue.visibility = View.GONE
        tt3IdNumberTextView.visibility = View.GONE
        tt3IdNumberTextViewValue.visibility = View.GONE
        tt3CreditorABSNTextView.visibility = View.GONE
        tt3CreditorABSNTextViewValue.visibility = View.GONE
        tt3MaxCollectionAmountTextView.visibility = View.GONE
        tt3MaxCollectionAmountTextViewValue.visibility = View.GONE
        tt3ContractReferenceTextView.visibility = View.GONE
        tt3ContractReferenceTextViewValue.visibility = View.GONE
        tt3CollectionDayTextView.visibility = View.GONE
        tt3CollectionDayTextViewValue.visibility = View.GONE

        val transactionDetailsErrorImageView = findViewById<ImageView>(R.id.imageView2)
        val transactionDetailsErrorTextView = findViewById<TextView>(R.id.transaction_details_error_tv)
        val transactionDetailsErrorSubTextView = findViewById<TextView>(R.id.transaction_details_error_sub_tv)

        transactionDetailsErrorImageView.visibility = View.GONE
        transactionDetailsErrorTextView.visibility = View.GONE
        transactionDetailsErrorSubTextView.visibility = View.GONE

        val doneButton = findViewById<Button>(R.id.done_btn)

        val transactionType = intent.getStringExtra("TransactionType")
        val transactionId = intent.getStringExtra("TransactionId")

        var transactionDetails = TransactionDetails()
        var tt3TransactionDetails = TT3TransactionDetails()

        if (transactionId != null) {
            if (transactionType == "cardPaymentsIntent" || transactionType == "cardPaymentsDeeplink") {
                api.postTransactionDetails(
                    transactionId
                ) { transactionDetail ->
                    if(transactionDetail != null && transactionDetail != "" && transactionDetail != "401") {
                        val tdType = object : TypeToken<TransactionDetails>() { }.type
                        transactionDetails= Gson().fromJson<TransactionDetails>(transactionDetail, tdType)
                        this.runOnUiThread(java.lang.Runnable {
                            idTextView.visibility = View.VISIBLE
                            idTextViewValue.visibility = View.VISIBLE
                            passthroughUserIdTextView.visibility = View.VISIBLE
                            passthroughUserIdTextViewValue.visibility = View.VISIBLE
                            merchantTransactionReferenceTextView.visibility = View.VISIBLE
                            merchantTransactionReferenceTextViewValue.visibility = View.VISIBLE
                            encryptedV2.visibility = View.VISIBLE
                            jweTextView.visibility = View.VISIBLE
                            jweTextViewValue.visibility = View.VISIBLE
                            keyVersionTextView.visibility = View.VISIBLE
                            keyVersionTextViewValue.visibility = View.VISIBLE
                            fingerprintTextView.visibility = View.VISIBLE
                            fingerprintTextViewValue.visibility = View.VISIBLE
                            seeMoreButton1.visibility = View.VISIBLE

//                            val formattedMerchantAddress = transactionDetails.receiptData?.formattedMerchantAddress
                            val formattedMerchantAddress: List<String>? = transactionDetails.receiptData?.formattedMerchantAddress?.split("\n")
                            if (formattedMerchantAddress?.size!! > 0) {
                                var previousTextView: TextView = TextView(this)
                                previousTextView.text = ""

                                for (address in formattedMerchantAddress) {
                                    val transactionLayout= findViewById<ConstraintLayout>(R.id.transaction_details_layout)
                                    var dynamicTextView = TextView(this)

                                    dynamicTextView.id = View.generateViewId()
                                    dynamicTextView.text = address
                                    dynamicTextView.setTextColor(Color.BLACK)
                                    dynamicTextView.layoutParams = ConstraintLayout.LayoutParams(
                                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                                    )

                                    if (previousTextView.text == "") {
                                        val params = dynamicTextView.layoutParams as ConstraintLayout.LayoutParams
                                        params.topToBottom = formattedMerchantAddressTextViewValue.id
                                        dynamicTextView.requestLayout()
                                        previousTextView = dynamicTextView

                                        val param = dynamicTextView.layoutParams as ViewGroup.MarginLayoutParams
                                        param.setMargins(
                                            resources.getDimensionPixelOffset(R.dimen.zero),
                                            resources.getDimensionPixelOffset(R.dimen.textTop),
                                            resources.getDimensionPixelOffset(R.dimen.margin),
                                            resources.getDimensionPixelOffset(R.dimen.zero),
                                        )
                                        dynamicTextView.layoutParams = param
                                    } else {
                                        val params = dynamicTextView.layoutParams as ConstraintLayout.LayoutParams
                                        params.topToBottom = previousTextView.id
                                        dynamicTextView.requestLayout()
                                        previousTextView = dynamicTextView

                                        val param = dynamicTextView.layoutParams as ViewGroup.MarginLayoutParams
                                        param.setMargins(
                                            resources.getDimensionPixelOffset(R.dimen.zero),
                                            resources.getDimensionPixelOffset(R.dimen.textTop),
                                            resources.getDimensionPixelOffset(R.dimen.margin),
                                            resources.getDimensionPixelOffset(R.dimen.zero),
                                        )
                                        dynamicTextView.layoutParams = param
                                    }

                                    transactionLayout.addView(dynamicTextView)

                                    val params2 = transactionTypeTextView.layoutParams as ConstraintLayout.LayoutParams
                                    params2.topToBottom = dynamicTextView.id
                                    transactionTypeTextView.requestLayout()
                                }
                            } else {
                                formattedMerchantAddressTextViewValue.text = ""
                            }

                            idTextViewValue.text = transactionDetails.id
                            passthroughUserIdTextViewValue.text = transactionDetails.passthroughUserId
                            merchantTransactionReferenceTextViewValue.text = transactionDetails.merchantTransactionReference
                            jweTextViewValue.text = transactionDetails.details?.get(0)?.encryptedV2?.jwe
                            keyVersionTextViewValue.text = transactionDetails.details?.get(0)?.encryptedV2?.keyVersion
                            fingerprintTextViewValue.text = transactionDetails.details?.get(0)?.encryptedV2?.fingerprint
                            latitudeTextViewValue.text = transactionDetails.latitude
                            longitudeTextViewValue.text = transactionDetails.longitude
                            deviceInstallationIdTextViewValue.text = transactionDetails.deviceinstallationId
                            transactionstatusIdTextViewValue.text = transactionDetails.transactionstatusId.toString()
                            transactionDispositionIdTextViewValue.text = transactionDetails.transactionDispositionId.toString()
                            amountTextViewValue.text = transactionDetails.amount
//                        accountNumberTextViewValue.text = transactionDetails.accountNumber
                            currencyTextViewValue.text = transactionDetails.currency
                            responseCodeTextViewValue.text = transactionDetails.responseCode.toString()
                            transactionTypeIdTextViewValue.text = transactionDetails.transactionTypeId.toString()
                            originalTransactionIdTextViewValue.text = transactionDetails.originalTransactionId
                            acquirerIdTextViewValue.text = transactionDetails.acquirerId.toString()
                            userIdTextViewValue.text = transactionDetails.userId
                            transactionFeesConfigIdTextViewValue.text = transactionDetails.transactionFeesConfigId.toString()
                            TransactionFeeTextViewValue.text = transactionDetails.transactionFee
                            authorisationCodeTextViewValue.text = transactionDetails.authorisationCode
                            createdAtTextViewValue.text = transactionDetails.createdAt
                            updatedAtTextViewValue.text = transactionDetails.updatedAt
                            effectiveDateTextViewValue.text = transactionDetails.receiptData?.effectiveDate
                            expiryDateTextViewValue.text = transactionDetails.receiptData?.expiryDate
                            tidTextViewValue.text = transactionDetails.receiptData?.tid
                            transactionReferenceTextViewValue.text = transactionDetails.receiptData?.transactionReference
                            merchantNameTextViewValue.text = transactionDetails.receiptData?.merchantName
//                            formattedMerchantAddressTextViewValue.text = transactionDetails.receiptData?.formattedMerchantAddress
                            transactionTypeTextViewValue.text = transactionDetails.receiptData?.transactionType
                            approvalTextTextViewValue.text = transactionDetails.receiptData?.approvalText
                            aidTextViewValue.text = transactionDetails.receiptData?.aid
                            applicationLabelTextViewValue.text = transactionDetails.receiptData?.applicationLabel
                            applicationPreferredNameTextViewValue.text = transactionDetails.receiptData?.applicationPreferredName
                            associationTextViewValue.text = transactionDetails.receiptData?.association
                            isoResponseCodeTextViewValue.text = transactionDetails.receiptData?.isoResponseCode
                            cryptogramTextViewValue.text = transactionDetails.receiptData?.cryptogram
                            cryptogramTypeTextViewValue.text = transactionDetails.receiptData?.cryptogramType
                            tvrTextViewValue.text = transactionDetails.receiptData?.tvr
                            dateTextViewValue.text = transactionDetails.receiptData?.date
                            timeTextViewValue.text = transactionDetails.receiptData?.time
                            maskedPanTextViewValue.text = transactionDetails.receiptData?.maskedPan
                        })
                    } else {
                        this.runOnUiThread(java.lang.Runnable {
                            transactionDetailsErrorImageView.visibility = View.VISIBLE
                            transactionDetailsErrorTextView.visibility = View.VISIBLE
                            if (transactionDetail == "401") {
                                transactionDetailsErrorSubTextView.visibility = View.VISIBLE
                            }
                        })
                    }
                }
            }
            else if (transactionType == "debicheckIntent" || transactionType == "debicheckDeeplink") {
                api.getTT3TransactionDetails(
                    transactionId
                ) { transactionDetail ->
                    if(transactionDetail != null && transactionDetail != "" && transactionDetail != "401") {
                        val tt3tdType = object : TypeToken<TT3TransactionDetails>() { }.type
                        tt3TransactionDetails= Gson().fromJson<TT3TransactionDetails>(transactionDetail, tt3tdType)
                        this.runOnUiThread(java.lang.Runnable {
                            tt3QrCodeStateTextView.visibility = View.VISIBLE
                            tt3QrCodeStateTextViewValue.visibility = View.VISIBLE
                            tt3TransactionIdTextView.visibility = View.VISIBLE
                            tt3TransactionIdTextViewValue.visibility = View.VISIBLE
                            tt3MerchantTransactionReferenceTextView.visibility = View.VISIBLE
                            tt3MerchantTransactionReferenceTextViewValue.visibility = View.VISIBLE
                            tt3UserIdTextView.visibility = View.VISIBLE
                            tt3UserIdTextViewValue.visibility = View.VISIBLE
                            tt3StatusTextView.visibility = View.VISIBLE
                            tt3StatusTextViewValue.visibility = View.VISIBLE
                            tt3DispositionTextView.visibility = View.VISIBLE
                            tt3DispositionTextViewValue.visibility = View.VISIBLE
                            tt3AmountTextView.visibility = View.VISIBLE
                            tt3AmountTextViewValue.visibility = View.VISIBLE
                            tt3CurrencyTextView.visibility = View.VISIBLE
                            tt3CurrencyTextViewValue.visibility = View.VISIBLE
                            tt3TypeTextView.visibility = View.VISIBLE
                            tt3TypeTextViewValue.visibility = View.VISIBLE
                            tt3ResponseCodeTextView.visibility = View.VISIBLE
                            tt3ResponseCodeTextViewValue.visibility = View.VISIBLE
                            tt3AuthorisationCodeTextView.visibility = View.VISIBLE
                            tt3AuthorisationCodeTextViewValue.visibility = View.VISIBLE

                            seeMoreButton4.visibility = View.VISIBLE

                            tt3QrCodeStateTextViewValue.text = tt3TransactionDetails.qrCodeState
                            tt3TransactionIdTextViewValue.text = tt3TransactionDetails.transactionId
                            tt3MerchantTransactionReferenceTextViewValue.text = tt3TransactionDetails.merchantTransactionReference
                            tt3UserIdTextViewValue.text = tt3TransactionDetails.userId
                            tt3StatusTextViewValue.text = tt3TransactionDetails.status
                            tt3DispositionTextViewValue.text = tt3TransactionDetails.disposition
                            tt3AmountTextViewValue.text = tt3TransactionDetails.amount
                            tt3CurrencyTextViewValue.text = tt3TransactionDetails.currency
                            tt3TypeTextViewValue.text = tt3TransactionDetails.type
                            tt3ResponseCodeTextViewValue.text = tt3TransactionDetails.responseCode
                            tt3AuthorisationCodeTextViewValue.text = tt3TransactionDetails.authorisationCode
                            tt3CreatedAtTextViewValue.text = tt3TransactionDetails.createdAt
                            tt3UpdatedAtTextViewValue.text = tt3TransactionDetails.updatedAt

                            tt3AccountNumberTextViewValue.text =  if (tt3TransactionDetails.accountNumber != "") tt3TransactionDetails.accountNumber else activeProfile.activeRequest?.accountNumber
                            tt3IdNumberTextViewValue.text = if (tt3TransactionDetails.idNumber != "") tt3TransactionDetails.idNumber else activeProfile.activeRequest?.idNumber
                            tt3CreditorABSNTextViewValue.text = if (tt3TransactionDetails.creditorABSN != "") tt3TransactionDetails.creditorABSN else activeProfile.activeRequest?.creditor
                            tt3MaxCollectionAmountTextViewValue.text = if (tt3TransactionDetails.maxCollectionAmount != "") tt3TransactionDetails.maxCollectionAmount else activeProfile.activeRequest?.maxCollectionAmount
                            tt3ContractReferenceTextViewValue.text = if (tt3TransactionDetails.contractReference != "") tt3TransactionDetails.contractReference else activeProfile.activeRequest?.contractReference
                            tt3CollectionDayTextViewValue.text = if (tt3TransactionDetails.collectionDay != "") tt3TransactionDetails.collectionDay else activeProfile.activeRequest?.debitOrderDay.toString()
                        })
                    } else {
                        this.runOnUiThread(java.lang.Runnable {
                            transactionDetailsErrorImageView.visibility = View.VISIBLE
                            transactionDetailsErrorTextView.visibility = View.VISIBLE
                            if (transactionDetail == "401") {
                                transactionDetailsErrorSubTextView.visibility = View.VISIBLE
                            }
                        })
                    }
                }
            }
        } else {
            transactionDetailsErrorImageView.visibility = View.VISIBLE
            transactionDetailsErrorTextView.visibility = View.VISIBLE
        }

        seeMoreButton1.setOnClickListener {
            seeMoreButton1.visibility = View.GONE

            latitudeTextView.visibility = View.VISIBLE
            latitudeTextViewValue.visibility = View.VISIBLE
            longitudeTextView.visibility = View.VISIBLE
            longitudeTextViewValue.visibility = View.VISIBLE
            deviceInstallationIdTextView.visibility = View.VISIBLE
            deviceInstallationIdTextViewValue.visibility = View.VISIBLE
            transactionstatusIdTextView.visibility = View.VISIBLE
            transactionstatusIdTextViewValue.visibility = View.VISIBLE
            transactionDispositionIdTextView.visibility = View.VISIBLE
            transactionDispositionIdTextViewValue.visibility = View.VISIBLE
            amountTextView.visibility = View.VISIBLE
            amountTextViewValue.visibility = View.VISIBLE
            accountNumberTextView.visibility = View.VISIBLE
            accountNumberTextViewValue.visibility = View.VISIBLE
            currencyTextView.visibility = View.VISIBLE
            currencyTextViewValue.visibility = View.VISIBLE
            responseCodeTextView.visibility = View.VISIBLE
            responseCodeTextViewValue.visibility = View.VISIBLE
            transactionTypeIdTextView.visibility = View.VISIBLE
            transactionTypeIdTextViewValue.visibility = View.VISIBLE
            originalTransactionIdTextView.visibility = View.VISIBLE
            originalTransactionIdTextViewValue.visibility = View.VISIBLE
            acquirerIdTextView.visibility = View.VISIBLE
            acquirerIdTextViewValue.visibility = View.VISIBLE
            userIdTextView.visibility = View.VISIBLE
            userIdTextViewValue.visibility = View.VISIBLE
            transactionFeesConfigIdTextView.visibility = View.VISIBLE
            transactionFeesConfigIdTextViewValue.visibility = View.VISIBLE
            TransactionFeeTextView.visibility = View.VISIBLE
            TransactionFeeTextViewValue.visibility = View.VISIBLE
            authorisationCodeTextView.visibility = View.VISIBLE
            authorisationCodeTextViewValue.visibility = View.VISIBLE
            seeMoreButton2.visibility = View.VISIBLE
        }

        seeMoreButton2.setOnClickListener {
            seeMoreButton2.visibility = View.GONE

            createdAtTextView.visibility = View.VISIBLE
            createdAtTextViewValue.visibility = View.VISIBLE
            updatedAtTextView.visibility = View.VISIBLE
            updatedAtTextViewValue.visibility = View.VISIBLE
            effectiveDateTextView.visibility = View.VISIBLE
            effectiveDateTextViewValue.visibility = View.VISIBLE
            expiryDateTextView.visibility = View.VISIBLE
            expiryDateTextViewValue.visibility = View.VISIBLE
            tidTextView.visibility = View.VISIBLE
            tidTextViewValue.visibility = View.VISIBLE
            transactionReferenceTextView.visibility = View.VISIBLE
            transactionReferenceTextViewValue.visibility = View.VISIBLE
            merchantNameTextView.visibility = View.VISIBLE
            merchantNameTextViewValue.visibility = View.VISIBLE
            formattedMerchantAddressTextView.visibility = View.VISIBLE
            formattedMerchantAddressTextViewValue.visibility = View.VISIBLE
            transactionTypeTextView.visibility = View.VISIBLE
            transactionTypeTextViewValue.visibility = View.VISIBLE
            approvalTextTextView.visibility = View.VISIBLE
            approvalTextTextViewValue.visibility = View.VISIBLE
            aidTextView.visibility = View.VISIBLE
            aidTextViewValue.visibility = View.VISIBLE
            seeMoreButton3.visibility = View.VISIBLE
        }

        seeMoreButton3.setOnClickListener {
            seeMoreButton3.visibility = View.GONE

            applicationLabelTextView.visibility = View.VISIBLE
            applicationLabelTextViewValue.visibility = View.VISIBLE
            applicationPreferredNameTextView.visibility = View.VISIBLE
            applicationPreferredNameTextViewValue.visibility = View.VISIBLE
            associationTextView.visibility = View.VISIBLE
            associationTextViewValue.visibility = View.VISIBLE
            isoResponseCodeTextView.visibility = View.VISIBLE
            isoResponseCodeTextViewValue.visibility = View.VISIBLE
            cryptogramTextView.visibility = View.VISIBLE
            cryptogramTextViewValue.visibility = View.VISIBLE
            cryptogramTypeTextView.visibility = View.VISIBLE
            cryptogramTypeTextViewValue.visibility = View.VISIBLE
            tvrTextView.visibility = View.VISIBLE
            tvrTextViewValue.visibility = View.VISIBLE
            dateTextView.visibility = View.VISIBLE
            dateTextViewValue.visibility = View.VISIBLE
            timeTextView.visibility = View.VISIBLE
            timeTextViewValue.visibility = View.VISIBLE
            maskedPanTextView.visibility = View.VISIBLE
            maskedPanTextViewValue.visibility = View.VISIBLE
        }

        seeMoreButton4.setOnClickListener {
            seeMoreButton4.visibility = View.GONE

            tt3CreatedAtTextView.visibility = View.VISIBLE
            tt3CreatedAtTextViewValue.visibility = View.VISIBLE
            tt3UpdatedAtTextView.visibility = View.VISIBLE
            tt3UpdatedAtTextViewValue.visibility = View.VISIBLE
            tt3AccountNumberTextView.visibility = View.VISIBLE
            tt3AccountNumberTextViewValue.visibility = View.VISIBLE
            tt3IdNumberTextView.visibility = View.VISIBLE
            tt3IdNumberTextViewValue.visibility = View.VISIBLE
            tt3CreditorABSNTextView.visibility = View.VISIBLE
            tt3CreditorABSNTextViewValue.visibility = View.VISIBLE
            tt3MaxCollectionAmountTextView.visibility = View.VISIBLE
            tt3MaxCollectionAmountTextViewValue.visibility = View.VISIBLE
            tt3ContractReferenceTextView.visibility = View.VISIBLE
            tt3ContractReferenceTextViewValue.visibility = View.VISIBLE
            tt3CollectionDayTextView.visibility = View.VISIBLE
            tt3CollectionDayTextViewValue.visibility = View.VISIBLE
        }

        doneButton.setOnClickListener {
            finish()
        }
    }
}