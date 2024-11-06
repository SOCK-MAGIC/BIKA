plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.shizq.bika.core.datastore"
}

dependencies {
    api(libs.androidx.datastore)
    api(projects.core.model)

    implementation(projects.core.common)

    implementation(libs.kotlinx.serialization.json)
}
