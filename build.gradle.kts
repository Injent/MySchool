buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.kotlin.android") apply false
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.KOTLIN apply false
    id("com.google.dagger.hilt.android") version Versions.HILT apply false
    id("org.jetbrains.kotlin.jvm") apply false
}