plugins {
    id("kotlin-kapt")
    alias(libs.plugins.bika.android.application)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.shizq.bika"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shizq.bika"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "1.0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("keyStore") {
            storeFile = file("appkey.jks")
            storePassword = "123456"
            keyAlias = "shizq"
            keyPassword = "123456"
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("keyStore")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("keyStore")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "retrofit2.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    applicationVariants.all {
        outputs
            .map { it as com.android.build.gradle.internal.api.ApkVariantOutputImpl }
            .forEach { output ->
                output.outputFileName = "BIKA_v$versionName.apk"
            }
    }
}

dependencies {
    implementation(projects.feature.comic)
    implementation(projects.feature.interest)
    implementation(projects.feature.reader)
    implementation(projects.feature.signin)
    implementation(projects.feature.signup)
    implementation(projects.feature.splash)

    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.sync.work)

    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    ksp(libs.androidx.room.compiler)

    implementation(libs.byrecyclerview)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.retrofit.converter.kotlinx.serialization)

    implementation(libs.glide)
    implementation(libs.glide.okhttp3)

    implementation(libs.rxandroid)

    implementation(libs.pictureselector)
    implementation(libs.ucrop)

    implementation(libs.photoview)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.commons.codec)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.viewbinding)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
//    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
//    implementation(libs.androidx.window.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    implementation(libs.coil.kt)
    implementation(libs.okhttp)
    implementation(libs.coil.network)
}
