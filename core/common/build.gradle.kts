plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.hilt)
}

dependencies {
    implementation("app.cash.molecule:molecule-runtime:2.0.0")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.decompose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
}
