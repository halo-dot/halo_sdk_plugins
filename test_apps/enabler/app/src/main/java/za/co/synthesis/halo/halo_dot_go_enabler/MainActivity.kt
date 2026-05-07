package za.co.synthesis.halo.halo_dot_go_enabler

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    private lateinit var clickedButton: Button
    private lateinit var previouslyClickedButtonEnv: String

    private var profiles = mutableListOf<Profile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createProfileButton = findViewById<Button>(R.id.btnCreateProfile)

        //Get values if they exist
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val gson = Gson()
        val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
//        var profiles = mutableListOf<Profile>()

        if (jsonProfile != null && jsonProfile != ""){
            val sType = object : TypeToken<List<Profile>>() { }.type
            profiles = gson.fromJson<MutableList<Profile>>(jsonProfile, sType)
        }

        var continueButton = findViewById<View>(R.id.continue_btn)

        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
        } else {
            continueButton.isEnabled = false
            continueButton.setBackgroundResource(R.drawable.disabled_button)
        }

        val haloLogoImageView = findViewById<ImageView>(R.id.imageView)
        var divider = findViewById<View>(R.id.divider_1)
        var editProfileButton = findViewById<View>(R.id.edit_btn)

        var profileButton = findViewById<Button>(R.id.btnProfile)
        profileButton.visibility = View.GONE

        if(profiles.size > 0) {
            continueButton.visibility = View.VISIBLE
            editProfileButton.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            createProfileButton.visibility = View.VISIBLE

            var previousButton: Button = Button(this)
            previousButton.text = ""

            for (profile in profiles) {
                val mainLayout= findViewById<ConstraintLayout>(R.id.mainLayout2)
                var dynamicButton = Button(this)

                dynamicButton.id = View.generateViewId()
                dynamicButton.text = profile.name
                dynamicButton.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                dynamicButton.isAllCaps = false
                dynamicButton.setTextColor(Color.BLACK)
                dynamicButton.setBackgroundResource(R.drawable.home_screen_button)

                dynamicButton.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )

                dynamicButton.updatePadding(
                    resources.getDimensionPixelOffset(R.dimen.padding),
                    resources.getDimensionPixelOffset(R.dimen.padding),
                    resources.getDimensionPixelOffset(R.dimen.padding),
                    resources.getDimensionPixelOffset(R.dimen.padding)
                )

                if(profile.haloEnvironment == "dev") {
                    dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_dev, 0, 0, 0)
                } else if(profile.haloEnvironment == "qa") {
                    dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_qa, 0, 0, 0)
                } else if(profile.haloEnvironment == "prod") {
                    dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_prod, 0, 0, 0)
                }

                dynamicButton.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.padding)

                if(previousButton.text == "") {
                    val params = dynamicButton.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = haloLogoImageView.id
                    dynamicButton.requestLayout()
                    previousButton = dynamicButton

                    val param = dynamicButton.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(
                        resources.getDimensionPixelOffset(R.dimen.margin),
                        resources.getDimensionPixelOffset(R.dimen.marginTopInitials),
                        resources.getDimensionPixelOffset(R.dimen.margin),
                        resources.getDimensionPixelOffset(R.dimen.margin),
                    )
                    dynamicButton.layoutParams = param
                } else {
                    val params = dynamicButton.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = previousButton.id
                    dynamicButton.requestLayout()
                    previousButton = dynamicButton

                    val param = dynamicButton.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(
                        resources.getDimensionPixelOffset(R.dimen.margin),
                        resources.getDimensionPixelOffset(R.dimen.margin),
                        resources.getDimensionPixelOffset(R.dimen.margin),
                        resources.getDimensionPixelOffset(R.dimen.margin),
                    )
                    dynamicButton.layoutParams = param
                }

                dynamicButton.setOnClickListener { view ->
                    //your desired functionality
                    handleClicks(view, profiles)
                };

                mainLayout.addView(dynamicButton)

                val params2 = divider.layoutParams as ConstraintLayout.LayoutParams
                params2.topToBottom = dynamicButton.id
                divider.requestLayout()

                if (activeProfile.name != "" && activeProfile.name == profile.name) {
                    clickedButton = dynamicButton
                    previouslyClickedButtonEnv = profile.haloEnvironment
                    if(profile.haloEnvironment == "dev") {
                        dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_dev, 0, R.drawable.ic_check_line, 0)
                    } else if(profile.haloEnvironment == "qa") {
                        dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_qa, 0, R.drawable.ic_check_line, 0)
                    } else if(profile.haloEnvironment == "prod") {
                        dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_prod, 0, R.drawable.ic_check_line, 0)
                    }
                }
            }
        } else {
            continueButton.visibility = View.GONE
            editProfileButton.visibility = View.GONE
            divider.visibility = View.GONE
            createProfileButton.visibility = View.VISIBLE
        }

        profileButton.setOnClickListener {
            for (profile in profiles) {
                if (profile.name == profileButton.text) {
                    activeProfile = profile

                    val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
                    prefsEditor.putString("ActiveProfile", gson.toJson(activeProfile))
                    prefsEditor.commit()
                    prefsEditor.apply()
                }
            }
        }

        createProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        continueButton.setOnClickListener {
            val intent = Intent(this, ApiRequestActivity::class.java)
            startActivity(intent)
        }

        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("Action", "Edit")
            startActivityForResult(intent, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val gson = Gson()
        val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
//        var profiles = mutableListOf<Profile>()

        if (jsonProfile != null && jsonProfile != ""){
            val sType = object : TypeToken<List<Profile>>() { }.type
            profiles = gson.fromJson<MutableList<Profile>>(jsonProfile, sType)
        }
    }

    private fun handleClicks(view: View, profiles: MutableList<Profile>) {
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = Gson().fromJson(jsonActiveProfile, Profile::class.java)
        }

        if(this::clickedButton.isInitialized) {
            if(previouslyClickedButtonEnv == "dev") {
                clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_dev, 0, 0, 0)
            } else if(previouslyClickedButtonEnv == "qa") {
                clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_qa, 0, 0, 0)
            } else if(previouslyClickedButtonEnv == "prod") {
                clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_prod, 0, 0, 0)
            }
        }

        clickedButton = findViewById<Button>(view.id)

        for (profile in profiles) {
            if (profile.name == clickedButton.text) {
                previouslyClickedButtonEnv = profile.haloEnvironment

                if(profile.haloEnvironment == "dev") {
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_dev, 0, R.drawable.ic_check_line, 0)
                } else if(profile.haloEnvironment == "qa") {
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_qa, 0, R.drawable.ic_check_line, 0)
                } else if(profile.haloEnvironment == "prod") {
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_env_prod, 0, R.drawable.ic_check_line, 0)
                }

                activeProfile = profile

                val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
                prefsEditor.putString("ActiveProfile", Gson().toJson(activeProfile))
                prefsEditor.commit()
                prefsEditor.apply()
            }
        }

        var continueButton = findViewById<View>(R.id.continue_btn)
        continueButton.isEnabled = true
        continueButton.setBackgroundResource(R.drawable.gradient_button)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            this.recreate()
        }
    }
}