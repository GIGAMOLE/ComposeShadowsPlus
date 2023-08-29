plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    setup()

    kotlinOptions {
        jvmTarget = ProjectConfig.kotlinJvmTarget
    }
}