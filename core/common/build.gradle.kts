plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.core.common"
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.napier)

    implementation(libs.androidx.compose.runtime)
}
