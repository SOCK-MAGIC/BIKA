plugins {
    alias(libs.plugins.bika.android.feature)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.decompose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.feature.reader"
}

dependencies {
    implementation(projects.core.network)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}
