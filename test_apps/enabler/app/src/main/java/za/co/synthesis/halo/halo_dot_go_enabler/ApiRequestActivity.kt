package za.co.synthesis.halo.halo_dot_go_enabler

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ApiRequestActivity : AppCompatActivity() {

    private lateinit var clickedButton: Button
    private lateinit var editImageView: ImageView
    private var editRequestButtons: MutableList<String> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_request)

        setSupportActionBar(findViewById(R.id.api_request_toolbar))

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val gson = Gson()

        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)

        val jsonProfile: String? = sharedPreferences.getString("Profiles", "")
        var profiles = mutableListOf<Profile>()

        if (jsonProfile != null && jsonProfile != ""){
            val sType = object : TypeToken<List<Profile>>() { }.type
            profiles = gson.fromJson<MutableList<Profile>>(jsonProfile, sType)
        }

        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = gson.fromJson(jsonActiveProfile, Profile::class.java)
        }

        val profileToolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        val divider = findViewById<View>(R.id.divider_1)
        val requestButton = findViewById<Button>(R.id.btnRequest1)
        val addApiRequestButton = findViewById<Button>(R.id.btnAddAPIRequest)
        val continueButton = findViewById<Button>(R.id.continue_btn)
        val cancelButton = findViewById<Button>(R.id.cancel_btn)

        val requests = activeProfile.apiRequests

        profileToolbarTitle.text = activeProfile.name
        requestButton.text = requests?.get(0)?.name

        if(activeProfile.apiRequests != null) {
            requestButton.visibility = View.VISIBLE
        } else {
            requestButton.visibility = View.GONE
        }

        if (activeProfile.activeRequest != null) {
            continueButton.isEnabled = true
            continueButton.setBackgroundResource(R.drawable.gradient_button)
        } else {
            continueButton.isEnabled = false
            continueButton.setBackgroundResource(R.drawable.disabled_button)
        }

        addApiRequestButton.setOnClickListener {
            val intent = Intent(this, RequestActivity::class.java)
            intent.putExtra("Action", "Add")

            startActivityForResult(intent, 0)
        }

        requestButton.setOnClickListener {
            continueButton.isEnabled = true
            continueButton.setBackgroundResource(R.drawable.gradient_button)
            requestButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_logo, 0, R.drawable.ic_check_line, 0)
        }

        cancelButton.setOnClickListener {
            finish()
        }

        continueButton.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }

        if(activeProfile.apiRequests != null && activeProfile.apiRequests?.size!! > 0) {
            var previousButton: Button = Button(this)
            previousButton.text = ""

            for (request in activeProfile.apiRequests!!) {
                val mainLayout= findViewById<ConstraintLayout>(R.id.apiRequestLayout2)
                var dynamicButton = Button(this)

                dynamicButton.id = View.generateViewId()
                dynamicButton.text = request.name
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

                dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_logo, 0, R.drawable.ic_delete, 0)
                dynamicButton.tag = "Not Checked"

                dynamicButton.compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.padding)

                if(previousButton.text == "") {
                    val params = dynamicButton.layoutParams as ConstraintLayout.LayoutParams
                    params.topToBottom = R.id.api_requests_tv
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

                dynamicButton.setOnTouchListener { view, motionEvent ->
                    val DRAWABLE_LEFT = 0
                    val DRAWABLE_TOP = 1
                    val DRAWABLE_RIGHT = 2
                    val DRAWABLE_BOTTOM = 3

                    val drawableIconWidth = resources.getDrawable(R.drawable.ic_delete, theme).intrinsicWidth

                    if (motionEvent.action === MotionEvent.ACTION_UP) {
                        if (motionEvent.rawX >= dynamicButton.right - dynamicButton.compoundDrawables[DRAWABLE_RIGHT].bounds.width() - dynamicButton.compoundDrawablePadding) {
                            if (dynamicButton.tag != "Checked") {
                                val dialog = Dialog(this)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.custom_dialog)
                                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);

                                var requestDeleted = false

                                dialog.findViewById<Button>(R.id.delete_btn).setOnClickListener {
                                    dialog.dismiss()
                                    requestDeleted = handleDelete(view, profiles)
                                    if(requestDeleted) {
                                        this.recreate()
                                    }
                                }
                                dialog.findViewById<Button>(R.id.cancel_btn).setOnClickListener { dialog.dismiss() }

                                dialog.show()
                            }

                            return@setOnTouchListener true
                        } else if (motionEvent.rawX >= dynamicButton.right - drawableIconWidth - resources.getDimensionPixelOffset(R.dimen.textTop) - resources.getDimensionPixelOffset(R.dimen.margin) - resources.getDimensionPixelOffset(R.dimen.padding) && motionEvent.rawX <= dynamicButton.right - dynamicButton.compoundDrawables[DRAWABLE_RIGHT].bounds.width() - dynamicButton.compoundDrawablePadding) {
                            val intent = Intent(this, RequestActivity::class.java)
                            intent.putExtra("Action", "Edit")
                            intent.putExtra("Request", dynamicButton.text)

                            startActivityForResult(intent, 0)
                            return@setOnTouchListener true
                        } else {
                            handleClicks(view, profiles)
                        }
                    }
                    false
                }

                mainLayout.addView(dynamicButton)

                var dynamicImageView = ImageView(this)
                dynamicImageView.id = View.generateViewId()
                dynamicImageView.setImageResource(R.drawable.ic_pencil_line)
                dynamicImageView.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                dynamicImageView.elevation = R.dimen.elevation.toFloat()
                mainLayout.addView(dynamicImageView)

                dynamicImageView.updatePadding(
                    0,
                    0,
                    resources.getDimensionPixelOffset(R.dimen.margin),
                    0
                )

                val params = dynamicImageView.layoutParams as ConstraintLayout.LayoutParams
                params.topToTop = dynamicButton.id
                params.bottomToBottom = dynamicButton.id
                params.endToEnd = dynamicButton.id
                dynamicImageView.requestLayout()

                val param = dynamicImageView.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(
                    0,
                    0,
                    resources.getDimensionPixelOffset(R.dimen.textTop),
                    0,
                )
                dynamicImageView.layoutParams = param

                val params2 = divider.layoutParams as ConstraintLayout.LayoutParams
                params2.topToBottom = dynamicButton.id
                divider.requestLayout()

                if (activeProfile.activeRequest?.name != "" && activeProfile.activeRequest?.name == request.name) {
                    dynamicButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_logo, 0, R.drawable.ic_check_line, 0)
                    dynamicButton.tag = "Checked"

                    clickedButton = dynamicButton
                    dynamicImageView.visibility = View.GONE
                    editImageView = dynamicImageView
                }

                editRequestButtons.add(dynamicButton.text.toString() + "," + dynamicImageView.id.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        this.recreate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleClicks(view: View, profiles: MutableList<Profile>) {
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = Gson().fromJson(jsonActiveProfile, Profile::class.java)
        }

        if(this::clickedButton.isInitialized) {
            clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_logo, 0, R.drawable.ic_delete, 0)
            clickedButton.tag = "Not Checked"
            editImageView.visibility = View.VISIBLE
        }

        clickedButton = findViewById<Button>(view.id)

        for (ivs in editRequestButtons) {
            val iv = ivs.split(",")
            if(clickedButton.text == iv[0]) {
                val id: Int = resources.getIdentifier(iv[1], "id", packageName)
                editImageView = findViewById(id)
                editImageView.visibility = View.GONE
            }
        }

        if(activeProfile.apiRequests != null  && activeProfile.apiRequests?.size!! > 0) {
            for (request in activeProfile.apiRequests!!) {
                if (request.name == clickedButton.text) {
                    clickedButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_logo, 0, R.drawable.ic_check_line, 0)
                    clickedButton.tag = "Checked"
                    activeProfile.activeRequest = request

                    for (profile in profiles) {
                        if (profile.name == activeProfile.name) {
                            profiles[profiles.indexOf(profile)].activeRequest = request
                        }
                    }

                    val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
                    prefsEditor.putString("Profiles", Gson().toJson(profiles))
                    prefsEditor.putString("ActiveProfile", Gson().toJson(activeProfile))
                    prefsEditor.commit()
                    prefsEditor.apply()

                    if(activeProfile.activeRequest != null) {
                        val continueButton = findViewById<Button>(R.id.continue_btn)
                        continueButton.isEnabled = true
                        continueButton.setBackgroundResource(R.drawable.gradient_button)
                    }
                }
            }
        }
    }

    private fun handleDelete(view: View, profiles: MutableList<Profile>): Boolean {
        var sharedPreferences = getSharedPreferences("za.co.synthesis.halo.halo_dot_go_enabler", MODE_PRIVATE)
        val jsonActiveProfile: String? = sharedPreferences.getString("ActiveProfile", "")
        var activeProfile: Profile = Profile("", "", "", "", "", "", "", null, null)
        if (jsonActiveProfile != null && jsonActiveProfile != ""){
            activeProfile = Gson().fromJson(jsonActiveProfile, Profile::class.java)
        }

        val requestToDelete = findViewById<Button>(view.id)
        var deleted = false

        if(activeProfile.apiRequests != null  && activeProfile.apiRequests?.size!! > 0) {
            for (request in activeProfile.apiRequests!!) {
                if (request.name == requestToDelete.text) {
                    activeProfile.apiRequests?.remove(request)

                    for (profile in profiles) {
                        if (profile.name == activeProfile.name) {
                            profiles[profiles.indexOf(profile)].apiRequests = activeProfile.apiRequests
                        }
                    }

                    val prefsEditor: SharedPreferences.Editor = sharedPreferences.edit()
                    prefsEditor.putString("Profiles", Gson().toJson(profiles))
                    prefsEditor.putString("ActiveProfile", Gson().toJson(activeProfile))
                    prefsEditor.commit()
                    prefsEditor.apply()

                    deleted = true
                    break
                }
            }
        }

        return deleted
    }
}