plugins {
    alias(libs.plugins.bika.android.feature)
    alias(libs.plugins.bika.android.compose)
    alias(libs.plugins.bika.decompose)
    alias(libs.plugins.bika.hilt)
}

android {
    namespace = "com.shizq.bika.feature.comic"
}

dependencies {
    implementation(projects.core.data)

    implementation(libs.androidx.paging.compose)

    implementation("com.webtoonscorp.android:readmore-material3:1.7.1")
}
