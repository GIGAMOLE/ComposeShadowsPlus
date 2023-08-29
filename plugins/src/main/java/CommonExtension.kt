@file:Suppress("UnstableApiUsage", "PackageDirectoryMismatch", "unused")

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

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

    buildFeatures {
        compose = true
    }
}

fun BaseAppModuleExtension.setup() {
    baseSetup()

    defaultConfig {
        compileSdk = ProjectConfig.compileSdk
    }

    buildFeatures {
        compose = true
    }
}