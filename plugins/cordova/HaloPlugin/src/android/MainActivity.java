package <%PACKAGE_NAME%>;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;

import org.apache.cordova.CordovaActivity;

import za.co.synthesis.halo.sdk.HaloSDK;

public class MainActivity extends CordovaActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        HaloSDK.Companion.onCreate(this , this, savedInstanceState, null);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
    }

    @Override
    public void onStart() {
        super.onStart();
        // your mobile app code here
        HaloSDK.Companion.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // your mobile app code here
        HaloSDK.Companion.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // your mobile app code here
        HaloSDK.Companion.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // your mobile app code here
        HaloSDK.Companion.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // your mobile app code here
        HaloSDK.Companion.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // your mobile app code here
        HaloSDK.Companion.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // your mobile app code here
        // HaloSDK.Companion.onDestroy();
    }
}
