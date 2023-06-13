@file:Suppress("UnstableApiUsage")

@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)

plugins {
    id("composeshadowsplus.library")
    id("maven-publish")
}

group = ProjectConfig.group
version = ProjectConfig.versionName

publishing {
    publications {
        register<MavenPublication>(ProjectConfig.publication) {
            groupId = ProjectConfig.group
            artifactId = ProjectConfig.artifact
            version = ProjectConfig.versionName

            afterEvaluate {
                from(components[ProjectConfig.publication])
            }
        }
    }
}

android {
    namespace = ProjectConfig.namespace

    defaultConfig {
        renderscriptTargetApi = ProjectConfig.targetSdk
        renderscriptSupportModeEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.version.get()
    }

    publishing {
        singleVariant(ProjectConfig.publication) {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.ktx)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    debugImplementation(libs.bundles.debug.compose)
}