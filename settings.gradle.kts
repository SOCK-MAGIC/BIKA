pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://www.jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://www.jitpack.io") }
    }
}

rootProject.name = "BIKA"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:network")
include(":feature:interest")
include(":sync:work")
