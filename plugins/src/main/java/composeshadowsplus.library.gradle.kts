plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    setup()

    kotlinOptions {
        jvmTarget = ProjectConfig.kotlinJvmTarget
    }
}
