import java.util.Properties

/**
 * The App Link domain, resolved the way the SDK's hosts are expected to resolve
 * it: the `applinkHost` gradle property first (part of the build invocation, so
 * a running daemon can't serve a stale one), else `APPLINK_HOST` from the
 * environment, else `applink.url` from local.properties, else the QA portal
 * this example's links come from.
 */
fun applinkHost(): String {
    (findProperty("applinkHost") as? String)?.takeIf { it.isNotBlank() }?.let { return it }
    System.getenv("APPLINK_HOST")?.takeIf { it.isNotBlank() }?.let { return it }
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        val properties = Properties()
        file.inputStream().use { properties.load(it) }
        properties.getProperty("applink.url")?.takeIf { it.isNotBlank() }?.let { return it }
    }
    return "go.merchantportal.qa.haloplus.io"
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "za.co.synthesis.halo.sdk_ui.example"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "za.co.synthesis.halo.sdk_ui.example"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue("string", "halo_applink_host", applinkHost())
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Debug-signed so the APK is installable when shared with testers.
            // Swap in a real keystore before any store distribution.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        // Carries the resValue above — the App Link domain.
        resValues = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // The library source, not the published artifact — the example exists
    // to exercise the code in this repo.
    implementation(project(":lib"))
//    implementation(libs.sdk.ui)
//    debugImplementation(libs.halo.debug.sdk)
//    releaseImplementation(libs.halo.sdk)
}
