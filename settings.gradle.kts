@file:Suppress("UnstableApiUsage")

include(":app")
include(":ComposeShadowsPlus")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        includeBuild("plugins")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ComposeShadowsPlusProject"
