@file:Suppress("UnstableApiUsage")
plugins {
    id("injent.android.feature")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.feature.auth"


}

dependencies {
    implementation(project(":core:auth"))
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
}