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
    api(projects.core.common)
    implementation(projects.core.datastore)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.okhttp)

    implementation(libs.coil.kt)
    implementation(libs.coil.network)

    implementation(libs.napier)

    implementation("org.minidns:minidns-hla:1.1.1")
}
