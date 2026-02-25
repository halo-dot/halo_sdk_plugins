package za.co.synthesis.halo.sdkreactnativeplugin

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.facebook.react.ReactActivity
import za.co.synthesis.halo.sdk.HaloSDK

/**
 * Abstract base activity for React Native apps using Halo SDK.
 *
 * Your app's MainActivity should extend HaloReactActivity instead of ReactActivity:
 *
 * ```kotlin
 * class MainActivity : HaloReactActivity() {
 *     override fun getMainComponentName() = "YourAppName"
 * }
 * ```
 *
 * This class handles the full HaloSDK lifecycle (onCreate, onStart, onResume, onPause, onStop)
 * and NFC foreground dispatch, mirroring the Flutter plugin's HaloActivity.
 */
abstract class HaloReactActivity : ReactActivity() {
    private val TAG = "HaloReactActivity"

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        if (nfcAdapter?.isEnabled == true) {
            HaloSDK.onCreate(this, this, savedInstanceState, null)
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        HaloSDK.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
        enableNfcForegroundDispatch()
        HaloSDK.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        HaloSDK.onPause()
        disableNfcForegroundDispatch()
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        HaloSDK.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState, outPersistentState)
        HaloSDK.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        HaloSDK.onSaveInstanceState(outState)
    }

    private fun enableNfcForegroundDispatch() {
        try {
            val intent = Intent(applicationContext, javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error enabling NFC foreground dispatch: ${ex.message}", ex)
        }
    }

    private fun disableNfcForegroundDispatch() {
        try {
            nfcAdapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error disabling NFC foreground dispatch: ${ex.message}", ex)
        }
    }
}
