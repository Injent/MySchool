@file:Suppress("UnstableApiUsage")
plugins {
    id("injent.android.application")
    id("injent.android.application.compose")
    id("injent.android.hilt")
    id("injent.android.application.firebase")
}

android {
    namespace = "me.injent.myschool"
    defaultConfig {
        applicationId = "me.injent.myschool"
        minSdk = Versions.MIN_ANDROID_SDK
        targetSdk = Versions.TARGET_ANDROID_SDK
        versionCode = 5
        versionName = "1.2.1" // X.Y.Z; X = Major, Y = Minor, Z = Patch level

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        val release = create("release") {
            storeFile = file("$projectDir/release.keystore")
            storePassword = "12345678"
            keyAlias = "release"
            keyPassword = "12345678"
        }
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        val debug by getting {
            isMinifyEnabled = false
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
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
    implementation(project(":core:analytics"))
    implementation(project(":core:auth"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))

    implementation(project(":feature:auth"))
    implementation(project(":feature:personmarks"))
    implementation(project(":feature:myclass"))
    implementation(project(":feature:leaderboard"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:statistics"))
    implementation(project(":feature:markdetails"))
    implementation(project(":feature:userprofile"))
    implementation(project(":feature:accounts"))

    implementation(project(":sync"))
    implementation(project(":updates"))

    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.ACTIVITY_COMPOSE)
    implementation(Dependencies.NAVIGATION_COMPOSE)
    implementation(Dependencies.CORE_SPLASHSCREEN)
    implementation(Dependencies.HILT_WORK)
    implementation(Dependencies.WINDOW_SIZE_CLASS)
    implementation(Dependencies.ACCOMPANIST_PERMISSIONS)
}