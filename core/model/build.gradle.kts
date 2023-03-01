plugins {
    id("injent.android.library")
    id("injent.kotlin.serialization")
}

android {
    namespace = "me.injent.myschool.core.model"
}

dependencies {
    implementation(Dependencies.DATETIME)
}