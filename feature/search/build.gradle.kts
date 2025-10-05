plugins {
    alias(libs.plugins.bika.android.feature)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.feature.search"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.database)

    implementation(libs.androidx.paging.compose)
}
