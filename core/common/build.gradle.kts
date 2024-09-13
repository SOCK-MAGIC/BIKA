plugins {
    alias(libs.plugins.bika.jvm.library)
    alias(libs.plugins.bika.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
}