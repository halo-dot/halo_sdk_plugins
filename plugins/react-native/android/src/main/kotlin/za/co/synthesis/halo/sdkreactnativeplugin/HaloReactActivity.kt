package za.co.synthesis.halo.sdkreactnativeplugin

import android.nfc.NfcAdapter
import android.os.Bundle
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
 * This class handles the full HaloSDK lifecycle (onCreate, onStart, onResume, onPause, onStop),
 * mirroring the Flutter plugin's HaloActivity.
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
        HaloSDK.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        HaloSDK.onPause()
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        HaloSDK.onStop()
        super.onStop()
    }
}
