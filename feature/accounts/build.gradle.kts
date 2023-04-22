plugins {
    id("injent.android.feature")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.feature.accounts"
}

dependencies {
    implementation(project(":core:auth"))
}