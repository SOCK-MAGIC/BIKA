plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.shizq.bika.core.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.kotlinx.serialization.json)
}
