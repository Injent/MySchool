plugins {
    id("injent.android.library")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.core.navigation"
}

dependencies {
    implementation(Dependencies.NAVIGATION_COMPOSE)
}