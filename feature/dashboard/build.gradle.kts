plugins {
    id("injent.android.feature")
    id("injent.android.library.compose")
}

android {
    namespace = "me.injent.myschool.feature.dashboard"
}

dependencies {
    implementation(project(":feature:homeworkdialog"))
}