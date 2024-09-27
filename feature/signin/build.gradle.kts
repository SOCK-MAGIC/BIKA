plugins {
    alias(libs.plugins.bika.android.feature)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.feature.signin"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    implementation(libs.decompose)
}
