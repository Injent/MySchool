plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.domain"
}

dependencies {
    implementation(project(":core:database"))
}