plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.auth"
}

dependencies {
    implementation(project(":core:data"))
}