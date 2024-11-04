plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.core.common"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.decompose)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.napier)
}
