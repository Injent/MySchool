package com.injent.schoolstat

import Versions
import com.android.build.api.dsl.CommonExtension
import compose
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        compileSdk = Versions.TARGET_ANDROID_SDK

        defaultConfig {
            minSdk = Versions.MIN_ANDROID_SDK
        }

        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = Versions.AndroidX.COMPOSE_COMPILER
        }

        dependencies {
            compose()
        }
    }
}