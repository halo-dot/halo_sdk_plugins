package za.co.synthesis.halo.sdkflutterplugin

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import io.flutter.embedding.android.FlutterFragmentActivity
import za.co.synthesis.halo.sdk.HaloSDK
import androidx.appcompat.app.AppCompatActivity

abstract class HaloActivity: FlutterFragmentActivity() {
    final val TAG = "HaloActivity"

    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        Log.d(TAG, "onCreate... 1")
//        super.onCreate(savedInstanceState, persistentState)
//        HaloSDK.onCreate(this , this, savedInstanceState, persistentState)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate... 2")
        super.onCreate(savedInstanceState)
        if (nfcAdapter?.isEnabled == true) {
            HaloSDK.onCreate(this , this, savedInstanceState, null)
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart...")
        super.onStart()
        HaloSDK.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume...")
        super.onResume()
        enableNfcForegroundDispatch(this)
        HaloSDK.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause...")
        HaloSDK.onPause()
        disableNfcForegroundDispatch()
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop...")
        HaloSDK.onStop()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d(TAG, "onSaveInstanceState...")
        super.onSaveInstanceState(outState, outPersistentState)
        HaloSDK.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState...")
        super.onSaveInstanceState(outState)
        HaloSDK.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy...")
        HaloSDK.onDestroy()
        super.onDestroy()
    }

    private fun enableNfcForegroundDispatch(activity: HaloActivity) {
        try {
            val intent = Intent(activity.applicationContext, activity.javaClass)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            val pendingIntent = PendingIntent.getActivity(
                activity.applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
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
