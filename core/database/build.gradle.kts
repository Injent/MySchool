plugins {
    id("injent.android.library")
    id("injent.android.hilt")
    id("injent.android.room")
}

android {
    namespace = "me.injent.myschool.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(Dependencies.DATETIME)
}