package za.co.synthesis.halo.halo_dot_go_enabler

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ProfileActivity : AppCompatActivity() {

    private var intentActionGlobal: String = ""
    private var profileNameGlobal: String = ""

    //Edit Texts Touched
    private var profileNameTouched = false
    private var merchantIDTouched = false
    private var apiKeyTouched = false
    private var usernameTouched = false
    private var passwordTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(findViewById(R.id.profile_toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checkFilled()

        //Get values if they exist
        val sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val gson = Gson()

        val profileNameErrorText = findViewById<TextView>(R.id.profile_name_error_tv)
        profileNameErrorText.visibility = View.GONE
        val usernameErrorText = findViewById<TextView>(R.id.username_error_tv)
        usernameErrorText.visibility = View.GONE
        val passwordErrorText = findViewById<TextView>(R.id.password_error_tv)
        passwordErrorText.visibility = View.GONE

        //Environment
        val devImageButton = findViewById<ImageButton>(R.id.env_dev_ib)
        val qaImageButton = findViewById<ImageButton>(R.id.env_qa_ib)
        val prodImageButton = findViewById<ImageButton>(R.id.env_prod_ib)
        var devChecked = false
        var qaChecked = false
        var prodChecked = false

        devImageButton.setOnClickListener {
            devChecked = true
            qaChecked = false
            prodChecked = false
            devImageButton.setImageResource(R.drawable.ic_env_dev_checked)
            devImageButton.tag = R.drawable.ic_env_dev_checked
            qaImageButton.setImageResource(R.drawable.ic_env_qa)
            qaImageButton.tag = R.drawable.ic_env_qa
            prodImageButton.setImageResource(R.drawable.ic_env_prod)
            prodImageButton.tag = R.drawable.ic_env_prod

            checkFilled()
        }
        qaImageButton.setOnClickListener {
            devChecked = false
            qaChecked = true
            prodChecked = false
            devImageButton.setImageResource(R.drawable.ic_env_dev)
            devImageButton.tag = R.drawable.ic_env_dev
            qaImageButton.setImageResource(R.drawable.ic_env_qa_checked)
            qaImageButton.tag = R.drawable.ic_env_qa_checked
            prodImageButton.setImageResource(R.drawable.ic_env_prod)
            prodImageButton.tag = R.drawable.ic_env_prod

            checkFilled()
        }
        prodImageButton.setOnClickListener {
            devChecked = false
            qaChecked = false
            prodChecked = true
            devImageButton.setImageResource(R.drawable.ic_env_dev)
            devImageButton.tag = R.drawable.ic_env_dev
            qaImageButton.setImageResource(R.drawable.ic_env_qa)
            qaImageButton.tag = R.drawable.ic_env_qa
            prodImageButton.setImageResource(R.drawable.ic_env_prod_checked)
            prodImageButton.tag = R.drawable.ic_env_prod_checked

            checkFilled()
        }

        //Switches
        val apiSwitch = findViewById<Switch>(R.id.api_key_switch)
        val unamePassSwitch = findViewById<Switch>(R.id.uname_pass_switch)

        checkChecked(apiSwitch.isChecked)

        //What to do when the switches are clicked
        apiSwitch.setOnClickListener{
            unamePassSwitch.isChecked = !apiSwitch.isChecked
            checkChecked(apiSwitch.isChecked)

            checkFilled()
        }
        unamePassSwitch.setOnClickListener{
            apiSwitch.isChecked = !unamePassSwitch.isChecked
            checkChecked(apiSwitch.isChecked)

            checkFilled()
        }

        //Edit Texts
        val profileNameEditText = findViewById<EditText>(R.id.profile_name_et)
        val merchantIDEditText = findViewById<EditText>(R.id.merchant_id_et)
        val apiKeyEditText = findViewById<EditText>(R.id.api_key_et)
        val usernameEditText = findViewById<EditText>(R.id.username_et)
        val passwordEditText = findViewById<EditText>(R.id.password_et)

        profileNameEditText.doOnTextChanged { text, start, before, count ->
            profileNameTouched = true
            checkFilled()
        }
        merchantIDEditText.doOnTextChanged { text, start, before, count ->
            merchantIDTouched = true
            checkFilled()
        }
        apiKeyEditText.doOnTextChanged { text, start, before, count ->
            apiKeyTouched = true
            checkFilled()
        }
        usernameEditText.doOnTextChanged { text, start, before, count ->
            usernameTouched = true
            checkFilled()
        }
        passwordEditText.doOnTextChanged { text, start, before, count ->
            passwordTouched = true
            checkFilled()
        }

        //Buttons
        val nextButton = findViewById<Button>(R.id.next_btn)
        nextButton.setOnClickListener {
            var env = ""
            if (devChecked) { env = "dev" }
            else if (qaChecked) { env = "qa" }
            else if (prodChecked) { env = "prod" }

            var auth = ""
            auth = if (apiSwitch.isChecked) {
                "apikey"
            } else {
                "unamepass"
            }

            val tempProfile = Profile(
                profileNameEditText.text.trim().toString(),
                merchantIDEditText.text.trim().toString(),
                env,
                usernameEditText.text.trim().toString(),
                passwordEditText.text.trim().toString(),
                apiKeyEditText.text.trim().toString(),
                auth,
                null,
                null
            )

            val tempProfileGson = gson.toJson(tempProfile)

            val intent = Intent(this, RequestActivity::class.java)
            intent.putExtra("tempProfile", tempProfileGson)
            startActivity(intent)
        }

        val clearButton = findViewById<Button>(R.id.clear_btn)
        clearButton.setOnClickListener {
            if (devChecked) {
                devImageButton.setImageResource(R.drawable.ic_env_dev)
                devImageButton.tag = R.drawable.ic_env_dev
                devChecked = false
            } else if (qaChecked) {
                qaImageButton.setImageResource(R.drawable.ic_env_qa)
                qaImageButton.tag = R.drawable.ic_env_qa
                qaChecked = false
            } else if (prodChecked) {
                prodImageButton.setImageResource(R.drawable.ic_env_prod)
                prodImageButton.tag = R.drawable.ic_env_prod
                prodChecked = false
            }

            findViewById<EditText>(R.id.profile_name_et).text.clear()
            findViewById<EditText>(R.id.merchant_id_et).text.clear()

            findViewById<EditText>(R.id.api_key_et).text.clear()
            findViewById<EditText>(R.id.username_et).text.clear()
            findViewById<EditText>(R.id.password_et).text.clear()
        }

        val deleteButton = findViewById<Button>(R.id.delete_btn)
        val saveButton = findViewById<Button>(R.id.save_btn)
        val cancelButton = findViewById<Button>(R.id.cancel_btn)
        deleteButton.visibility = View.GONE
        saveButton.visibility = View.GONE
        cancelButton.visibility = View.GONE

        saveButton.setOnClickListener {
            var env = ""
            if (devChecked) { env = "dev" }
            else if (qaChecked) { env = "qa" }
            else if (prodChecked) { env = "prod" }

            var auth = ""
            auth = if (apiSwitch.isChecked) {
                "apikey"
            } else {
                "unamepass"
            }

            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            var activeProfile = Profile("", "", "", "", "", "", "", null, null)
            if (jsonActiveProfile != null && jsonActiveProfile != ""){
                activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
            }

            val updatedProfile = Profile(
                profileNameEditText.text.trim().toString(),
                merchantIDEditText.text.trim().toString(),
                env,
                usernameEditText.text.trim().toString(),
                passwordEditText.text.trim().toString(),
                apiKeyEditText.text.trim().toString(),
                auth,
                activeProfile.apiRequests,
                activeProfile.activeRequest
            )

            val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
            var profiles = mutableListOf<Profile>()
            if (jsonProfile != null && jsonProfile != ""){
                val sType = object : TypeToken<List<Profile>>() { }.type
                profiles = gson.fromJson<MutableList<Profile>>(jsonProfile, sType)
            }
            for (profile in profiles) {
                if (profile.name == activeProfile.name) {
                    profiles[profiles.indexOf(profile)] = updatedProfile
                }
            }

            val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
            prefsEditor.putString("Profiles", gson.toJson(profiles))
            prefsEditor.putString("ActiveProfile", gson.toJson(updatedProfile))
            prefsEditor.commit()
            prefsEditor.apply()

            setResult(RESULT_OK)
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        val intentAction = intent.getStringExtra("Action")
        if (intentAction != null) {
            intentActionGlobal = intentAction
        }
        if(intentAction == "Edit") {
            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            val profileValues: Profile = gson.fromJson(jsonActiveProfile, Profile::class.java)

            val profileToolbarTitle = findViewById<TextView>(R.id.toolbar_title)
            profileToolbarTitle.text = profileValues.name
            profileNameGlobal = profileValues.name

            if (profileValues.haloEnvironment == "dev") {
                devChecked = true
                qaChecked = false
                prodChecked = false
                devImageButton.setImageResource(R.drawable.ic_env_dev_checked)
                devImageButton.tag = R.drawable.ic_env_dev_checked
                qaImageButton.setImageResource(R.drawable.ic_env_qa)
                qaImageButton.tag = R.drawable.ic_env_qa
                prodImageButton.setImageResource(R.drawable.ic_env_prod)
                prodImageButton.tag = R.drawable.ic_env_prod
            } else if (profileValues.haloEnvironment == "qa") {
                devChecked = false
                qaChecked = true
                prodChecked = false
                devImageButton.setImageResource(R.drawable.ic_env_dev)
                devImageButton.tag = R.drawable.ic_env_dev
                qaImageButton.setImageResource(R.drawable.ic_env_qa_checked)
                qaImageButton.tag = R.drawable.ic_env_qa_checked
                prodImageButton.setImageResource(R.drawable.ic_env_prod)
                prodImageButton.tag = R.drawable.ic_env_prod
            } else if (profileValues.haloEnvironment == "prod") {
                devChecked = false
                qaChecked = false
                prodChecked = true
                devImageButton.setImageResource(R.drawable.ic_env_dev)
                devImageButton.tag = R.drawable.ic_env_dev
                qaImageButton.setImageResource(R.drawable.ic_env_qa)
                qaImageButton.tag = R.drawable.ic_env_qa
                prodImageButton.setImageResource(R.drawable.ic_env_prod_checked)
                prodImageButton.tag = R.drawable.ic_env_prod_checked
            }

            profileNameEditText.setText(profileValues.name)
            merchantIDEditText.setText(profileValues.merchantId.toString())

            checkChecked(profileValues.authPreference == "apikey")

            if(profileValues.authPreference == "apikey") {
                apiSwitch.isChecked = true
                unamePassSwitch.isChecked = !apiSwitch.isChecked
            } else {
                apiSwitch.isChecked = false
                unamePassSwitch.isChecked = !apiSwitch.isChecked
            }

            apiKeyEditText.setText(profileValues.apiKey)
            usernameEditText.setText(profileValues.username)
            passwordEditText.setText(profileValues.password)

            nextButton.visibility = View.GONE
            clearButton.visibility = View.GONE
            deleteButton.visibility = View.VISIBLE
            saveButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE

            findViewById<ImageView>(R.id.progress_bar_1_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_2_iv).visibility = View.GONE
            findViewById<ImageView>(R.id.progress_bar_3_iv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_1_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_2_tv).visibility = View.GONE
            findViewById<TextView>(R.id.progress_bar_3_tv).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_1).visibility = View.GONE
            findViewById<View>(R.id.progress_bar_divider_2).visibility = View.GONE
        }

        deleteButton.setOnClickListener {
            val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
            val profileValues: Profile = gson.fromJson(jsonActiveProfile, Profile::class.java)

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);

            var requestDeleted = false

            dialog.findViewById<TextView>(R.id.dialog_tv).text = getString(R.string.profile_dialog_delete, profileValues.name)

            dialog.findViewById<Button>(R.id.delete_btn).setOnClickListener {
                dialog.dismiss()
                requestDeleted = handleDelete()
                if(requestDeleted) {
//                    this.recreate()
//                    val intent = Intent()
                    setResult(RESULT_OK)
                    finish()
                }
            }
            dialog.findViewById<Button>(R.id.cancel_btn).setOnClickListener { dialog.dismiss() }

            dialog.show()
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

        if (intentActionGlobal == "Edit") {
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
        val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
        var profiles = mutableListOf<Profile>()
        if (jsonProfile != null && jsonProfile != ""){
            val sType = object : TypeToken<List<Profile>>() { }.type
            profiles = gson.fromJson<MutableList<Profile>>(jsonProfile, sType)
        }

        var filled = true

        val devImageTag = findViewById<ImageButton>(R.id.env_dev_ib).tag
        val qaImageTag = findViewById<ImageButton>(R.id.env_qa_ib).tag
        val prodImageTag = findViewById<ImageButton>(R.id.env_prod_ib).tag

        if (devImageTag == R.drawable.ic_env_dev_checked || qaImageTag == R.drawable.ic_env_qa_checked || prodImageTag == R.drawable.ic_env_prod_checked) {

        } else {
            filled = false
        }

        val profileNameEditText = findViewById<EditText>(R.id.profile_name_et).text.trim()
        val merchantIDEditText = findViewById<EditText>(R.id.merchant_id_et).text.trim()
        val apiKeyEditText = findViewById<EditText>(R.id.api_key_et).text.trim()
        val usernameEditText = findViewById<EditText>(R.id.username_et).text.trim()
        val passwordEditText = findViewById<EditText>(R.id.password_et).text.trim()

        val apiSwitch = findViewById<Switch>(R.id.api_key_switch)

        if (TextUtils.isEmpty(profileNameEditText)) {
            if (profileNameTouched) {
                findViewById<EditText>(R.id.profile_name_et).setBackgroundResource(R.drawable.edit_text_border_red)
            }
            filled = false
        } else {
            var uniqueProfileName = true
            if (jsonProfile != null && jsonProfile != "") {
                if (profiles.isNotEmpty()) {
                    for (profile in profiles) {
                        if(profile.name.equals(profileNameEditText.toString(), true) && (profileNameGlobal != profile.name)) {
                            uniqueProfileName = false
                        }
                    }
                }
            }
            if (uniqueProfileName) {
                findViewById<EditText>(R.id.profile_name_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                findViewById<TextView>(R.id.profile_name_error_tv).visibility = View.GONE
            } else {
                findViewById<EditText>(R.id.profile_name_et).setBackgroundResource(R.drawable.edit_text_border_red)
                findViewById<TextView>(R.id.profile_name_error_tv).text = resources.getString(R.string.profile_name_error)
                findViewById<TextView>(R.id.profile_name_error_tv).visibility = View.VISIBLE
                filled = false
            }
        }

        if (TextUtils.isEmpty(merchantIDEditText)) {
            if (merchantIDTouched) {
                findViewById<EditText>(R.id.merchant_id_et).setBackgroundResource(R.drawable.edit_text_border_red)
            }
            filled = false
        } else {
            findViewById<EditText>(R.id.merchant_id_et).setBackgroundResource(R.drawable.edit_text_border_grey)
        }

        if (apiSwitch.isChecked) {
            if (TextUtils.isEmpty(apiKeyEditText)) {
                if (apiKeyTouched) {
                    findViewById<EditText>(R.id.api_key_et).setBackgroundResource(R.drawable.edit_text_border_red)
                }
                filled = false
            } else {
                findViewById<EditText>(R.id.api_key_et).setBackgroundResource(R.drawable.edit_text_border_grey)
            }
        } else {
            if (TextUtils.isEmpty(usernameEditText)) {
                if (usernameTouched) {
                    findViewById<EditText>(R.id.username_et).setBackgroundResource(R.drawable.edit_text_border_red)
                }
                filled = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(usernameEditText).matches() && (!usernameEditText.matches("[0-9]+".toRegex()) || usernameEditText.length != 10)) {
                if (usernameTouched) {
                    findViewById<EditText>(R.id.username_et).setBackgroundResource(R.drawable.edit_text_border_red)

                    val usernameErrorTextView = findViewById<TextView>(R.id.username_error_tv)
                    usernameErrorTextView.visibility = View.VISIBLE

                    if (usernameEditText.matches("[0-9]+".toRegex())) {
                        usernameErrorTextView.text = resources.getString(R.string.username_error_10_digits)
                    } else {
                        usernameErrorTextView.text = resources.getString(R.string.username_error_email)
                    }
                }
                filled = false
            } else {
                findViewById<EditText>(R.id.username_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                findViewById<TextView>(R.id.username_error_tv).visibility = View.GONE
            }

            if (TextUtils.isEmpty(passwordEditText)) {
                if (passwordTouched) {
                    findViewById<EditText>(R.id.password_et).setBackgroundResource(R.drawable.edit_text_border_red)
                }
                filled = false
            } else if (passwordEditText.length < 4){
                if (passwordTouched) {
                    findViewById<EditText>(R.id.password_et).setBackgroundResource(R.drawable.edit_text_border_red)
                    findViewById<TextView>(R.id.password_error_tv).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.password_error_tv).text = resources.getString(R.string.password_error_4_digits)
                }
                filled = false
            } else {
                findViewById<EditText>(R.id.password_et).setBackgroundResource(R.drawable.edit_text_border_grey)
                findViewById<TextView>(R.id.password_error_tv).visibility = View.GONE
            }
        }

        return filled
    }

    private fun checkChecked(checked: Boolean) {
        val apiKeyEditText = findViewById<EditText>(R.id.api_key_et)
        val usernameEditText = findViewById<EditText>(R.id.username_et)
        val passwordEditText = findViewById<EditText>(R.id.password_et)

        if(checked){
            apiKeyEditText.visibility = View.VISIBLE
            usernameEditText.visibility = View.GONE
            passwordEditText.visibility = View.GONE
        } else {
            apiKeyEditText.visibility = View.GONE
            usernameEditText.visibility = View.VISIBLE
            passwordEditText.visibility = View.VISIBLE
        }
    }

    private fun handleDelete(): Boolean {
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = Gson().fromJson(jsonActiveProfile, Profile::class.java)
        }

        val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
        var profiles = mutableListOf<Profile>()
        if (jsonProfile != null && jsonProfile != ""){
            val sType = object : TypeToken<List<Profile>>() { }.type
            profiles = Gson().fromJson<MutableList<Profile>>(jsonProfile, sType)
        }
        var deleted = false

        for (profile in profiles) {
            if (profile.name == activeProfile.name) {
                profiles.remove(profile)

                deleted = true
                break
            }
        }

        val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
        prefsEditor.putString("Profiles", Gson().toJson(profiles))
        prefsEditor.putString("ActiveProfile", "")
        prefsEditor.commit()
        prefsEditor.apply()

//        val requestToDelete = findViewById<Button>(view.id)

//        if(activeProfile.apiRequests != null  && activeProfile.apiRequests?.size!! > 0) {
//            for (request in activeProfile.apiRequests!!) {
//                if (request.name == requestToDelete.text) {
//                    activeProfile.apiRequests?.remove(request)
//
//                    for (profile in profiles) {
//                        if (profile.name == activeProfile.name) {
//                            profiles[profiles.indexOf(profile)].apiRequests = activeProfile.apiRequests
//                        }
//                    }
//
//                    val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
//                    prefsEditor.putString("Profiles", Gson().toJson(profiles))
//                    prefsEditor.putString("ActiveProfile", Gson().toJson(activeProfile))
//                    prefsEditor.commit()
//                    prefsEditor.apply()
//
//                    deleted = true
//                    break
//                }
//            }
//        }

        return deleted
    }
}