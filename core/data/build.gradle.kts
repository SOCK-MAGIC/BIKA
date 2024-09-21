plugins {
    alias(libs.plugins.bika.android.library)
    alias(libs.plugins.bika.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.shizq.bika.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)
}
