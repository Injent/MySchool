@file:Suppress("UnstableApiUsage")
plugins {
    id("injent.android.application")
    id("injent.android.application.compose")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool"
    defaultConfig {
        applicationId = "me.injent.myschool"
        minSdk = Versions.MIN_ANDROID_SDK
        targetSdk = Versions.TARGET_ANDROID_SDK
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        //testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        val debug by getting {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:authorization"))

    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.ACTIVITY_COMPOSE)
    implementation(Dependencies.NAVIGATION_COMPOSE)
    implementation(Dependencies.CORE_SPLASHSCREEN)
}