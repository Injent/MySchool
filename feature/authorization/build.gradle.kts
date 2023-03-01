plugins {
    id("injent.android.feature")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.feature.authorization"
}

dependencies {
    implementation(project(":core:common"))
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
}