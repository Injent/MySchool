buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// Definiton of all plugins used in the project
plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.KOTLIN apply false
    id("com.google.dagger.hilt.android") version Versions.HILT apply false
    id("org.jetbrains.kotlin.jvm") apply false
    id("com.google.devtools.ksp") apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") apply false
}