@file:Suppress("UnstableApiUsage")
plugins {
    id("injent.android.feature")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.authorization"
}

dependencies {
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
}