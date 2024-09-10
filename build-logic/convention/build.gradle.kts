plugins {
    alias(libs.plugins.kotlin.dsl)
}

group = "com.shizq.bika.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    implementation(libs.truth)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidCompose") {
            id = "bika.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "bika.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidJacoco") {
            id = "bika.android.jacoco"
            implementationClass = "AndroidJacocoConventionPlugin"
        }
        register("androidLibrary") {
            id = "bika.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "bika.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = "bika.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidRoom") {
            id = "bika.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidLint") {
            id = "bika.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "bika.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidFlavors") {
            id = "bika.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("androidApplicationTestOptionsUnitTests") {
            id = "bika.android.testoptions"
            implementationClass = "AndroidTestOptionsUnitTestsConventionPlugin"
        }
        register("hilt") {
            id = "bika.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("decompose") {
            id = "bika.decompose"
            implementationClass = "DecomposeConventionPlugin"
        }
        register("detekt") {
            id = libs.plugins.bika.detekt.get().pluginId
            implementationClass = "DetektConventionPlugin"
        }
        register("androidOptionsUnitTests") {
            id = "bika.android.testoptions"
            implementationClass = "AndroidTestOptionsUnitTestsConventionPlugin"
        }
    }
}