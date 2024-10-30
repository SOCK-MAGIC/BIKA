plugins {
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.bika.android.test)
}

android {
    namespace = "com.shizq.bika.benchmarks"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "APP_BUILD_TYPE_SUFFIX", "\"\"")
        buildConfigField("String", "APP_FLAVOR_SUFFIX", "\"\"")
    }
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }
    testOptions.managedDevices.devices {
        create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp"
        }
    }
    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)
    implementation(libs.androidx.test.uiautomator)
}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on.
    managedDevices.clear()
    managedDevices += "pixel6Api33"

    // Don't use a connected device but rely on a GMD for consistency between local and CI builds.
    useConnectedDevices = false
}
