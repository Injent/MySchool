plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.domain"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(Dependencies.DATETIME)
}