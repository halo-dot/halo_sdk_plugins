package za.co.synthesis.halo.sdkflutterplugin

import android.os.Bundle
import android.util.Log
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mastercard.sonic.controller.SonicController
import com.mastercard.sonic.controller.SonicType
import com.mastercard.sonic.listeners.OnCompleteListener
import com.mastercard.sonic.listeners.OnPrepareListener

import com.mastercard.sonic.controller.SonicEnvironment
import com.mastercard.sonic.model.SonicMerchant

import com.mastercard.sonic.widget.SonicView
import com.visa.SensoryBrandingView
import android.view.ViewGroup

class AnimationActivity : AppCompatActivity() {
    private val TAG = "AnimationActivity"

    private lateinit var sonicView: SonicView
    private var sonicController: SonicController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
            android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val maskedPAN = intent.getStringExtra(Const.MASKED_PAN)
        var cardAssociation: CardAssociations = try {
            intent.getStringExtra(Const.CARD_ASSOCIATION)?.let {
                CardAssociations.valueOf(it.uppercase())
            }
        } catch (e: Exception) {
            getCardTypeFromPan(maskedPAN)
        } ?: CardAssociations.UNKNOWN

        when (cardAssociation) {
            CardAssociations.VISA -> {

                val animationView: SensoryBrandingView = SensoryBrandingView(this, null).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    gravity = android.view.Gravity.CENTER
                }

                animationView.setConstrainedFlags(true)
                animationView.isSoundEnabled = true
                animationView.isHapticFeedbackEnabled = true
                animationView.isCheckMarkShown = true

                setContentView(animationView)

                animationView.post {
                    animationView.animate { error ->
                        closeAnimation()
                    }
                }

            }

            CardAssociations.MASTERCARD -> {
                sonicView = SonicView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                sonicController = SonicController();

                val sonicMerchant = SonicMerchant.Builder()
                    .merchantName("Halo")
                    .city("Pretoria")
                    .merchantCategoryCodes(arrayOf("MCC 5122"))
                    .countryCode("RSA")
                    .merchantId("halo-merc-1")
                    .build()

                sonicController?.prepare(
                    sonicType = SonicType.SOUND_AND_ANIMATION,
                    sonicCue = "checkout",
                    sonicEnvironment = SonicEnvironment.SANDBOX,
                    merchant = sonicMerchant,
                    isHapticsEnabled = true,
                    context = this,
                    onPrepareListener = object :
                        OnPrepareListener {
                        override fun onPrepared(statusCode: Int) {
                            playSonic()
                        }
                    })

                setContentView(sonicView)
            }

            else -> {finish()}
        }
    }

    private fun playSonic() {
        sonicController?.play(
            sonicView,
            object : OnCompleteListener {
                override fun onComplete(statusCode: Int) {
                    closeAnimation()
                }
            })
    }

    fun closeAnimation(){
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 3500)
    }

    private fun getCardTypeFromPan(maskedPan: String?): CardAssociations {
        return if (maskedPan != null) {
            when (maskedPan[0]) {
                '2' -> CardAssociations.MASTERCARD
                '5' -> CardAssociations.MASTERCARD
                '4' -> CardAssociations.VISA
                '3' -> CardAssociations.AMEX
                else -> CardAssociations.UNKNOWN
            }
        } else {
            CardAssociations.UNKNOWN
        }
    }

    override fun onBackPressed() {
    }

}

enum class CardAssociations(val association: String) {
    VISA("VISA"),
    MASTERCARD("MASTERCARD"),
    AMEX("AMERICAN EXPRESS"),
    UNKNOWN("UNKNOWN ASSOCIATION")
}
