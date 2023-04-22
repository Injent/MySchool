plugins {
    id("injent.android.library")
    id("injent.android.library.compose")
    id("injent.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "me.injent.myschool.core.analytics"
}

dependencies {
    implementation(platform(Dependencies.FIREBASE_BOM))
    implementation(Dependencies.FIREBASE_CRASHLYTICS)
    implementation(Dependencies.FIREBASE_ANALYTICS)
}