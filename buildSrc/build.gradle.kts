import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
    implementation(kotlin("gradle-plugin", "1.8.10"))
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.0-1.0.9")
}

gradlePlugin {
    plugins {
        register("androidApplicationComposePlugin") {
            id = "injent.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryComposePlugin") {
            id = "injent.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplicationPlugin") {
            id = "injent.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryPlugin") {
            id = "injent.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHiltPlugin") {
            id = "injent.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidFeaturePlugin") {
            id = "injent.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("kotlinSerializationPlugin") {
            id = "injent.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("androidRoomConventionPlugin") {
            id = "injent.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}