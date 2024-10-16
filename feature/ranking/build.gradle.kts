plugins {
    alias(libs.plugins.bika.android.feature)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.decompose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.feature.ranking"
}

dependencies {
    implementation(projects.core.network)
}
