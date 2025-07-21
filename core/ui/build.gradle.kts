plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.android.compose)
}

android {
    namespace = "com.shizq.bika.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.compose)
    implementation("androidx.navigation:navigation-compose:2.9.2")
}
