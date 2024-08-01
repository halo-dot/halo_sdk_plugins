package za.co.synthesis.halo.sdkflutterplugin

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import io.flutter.embedding.android.FlutterFragmentActivity
import za.co.synthesis.halo.sdk.HaloSDK

abstract class HaloActivity: FlutterFragmentActivity() {
    final val TAG = "HaloActivity"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(TAG, "onCreate... 1")
        super.onCreate(savedInstanceState, persistentState)
        HaloSDK.onCreate(this , this, savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate... 2")
        super.onCreate(savedInstanceState)
        HaloSDK.onCreate(this , this, savedInstanceState, null)
    }

    override fun onStart() {
        Log.d(TAG, "onStart...")
        super.onStart()
        HaloSDK.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume...")
        super.onResume()
        HaloSDK.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause...")
        super.onPause()
        HaloSDK.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop...")
        super.onStop()
        HaloSDK.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Log.d(TAG, "onSaveInstanceState...")
        super.onSaveInstanceState(outState, outPersistentState)
        HaloSDK.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy...")
        super.onDestroy()
        HaloSDK.onDestroy()
    }
}
