package za.co.synthesis.halo.sdkreactnativeplugin

import android.app.Activity
import java.lang.ref.WeakReference

object UIContext {
    private var activityRef: WeakReference<Activity>? = null
    private var enableSchemeAnimations: Boolean = false

    fun updateActivity(activity: Activity?) {
        activityRef = activity?.let { WeakReference(it) }
    }

    fun enableSchemeAnimations(enable: Boolean) {
        enableSchemeAnimations = enable
    }

    fun getActivity(): Activity? = activityRef?.get()
    fun areSchemeAnimationsEnabled(): Boolean = enableSchemeAnimations
}
