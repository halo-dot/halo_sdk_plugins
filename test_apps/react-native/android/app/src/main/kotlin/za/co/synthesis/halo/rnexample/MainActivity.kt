package za.co.synthesis.halo.rnexample

import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import za.co.synthesis.halo.sdkreactnativeplugin.HaloReactActivity

/**
 * The main entry point of the React Native app.
 *
 * Extends HaloReactActivity (instead of ReactActivity) so that the
 * Halo SDK lifecycle and NFC foreground dispatch are managed automatically.
 */
class MainActivity : HaloReactActivity() {

    override fun getMainComponentName(): String = "HaloRNExample"

    override fun createReactActivityDelegate() =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
