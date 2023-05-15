plugins {
    id("injent.android.library")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.core.designsystem"
}

dependencies {
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.ACCOMPANIST_SYSTEMUICONTROLLER)
    implementation(Dependencies.COIL)
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
}