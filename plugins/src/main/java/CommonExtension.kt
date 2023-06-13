@file:Suppress("UnstableApiUsage", "PackageDirectoryMismatch", "unused")

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.kotlin.dsl.accessors._404981569cb7bc4f1f0ba7441ad57f27.kotlinOptions
import gradle.kotlin.dsl.accessors._47545ee4044af277c92cdb30c1d58315.kotlinOptions

private fun BaseExtension.baseSetup() {
    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName
    }

    compileOptions {
        sourceCompatibility = ProjectConfig.javaCompileVersion
        targetCompatibility = ProjectConfig.javaCompileVersion
    }
}

fun LibraryExtension.setup() {
    baseSetup()

    defaultConfig {
        compileSdk = ProjectConfig.compileSdk
    }

    kotlinOptions {
        jvmTarget = ProjectConfig.kotlinJvmTarget
    }

    buildFeatures {
        compose = true
    }
}

fun BaseAppModuleExtension.setup() {
    baseSetup()

    defaultConfig {
        compileSdk = ProjectConfig.compileSdk
    }

    kotlinOptions {
        jvmTarget = ProjectConfig.kotlinJvmTarget
    }

    buildFeatures {
        compose = true
    }
}