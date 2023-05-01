plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.updates"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(Dependencies.FIREBASE_BOM)
    implementation(Dependencies.FIREBASE_FIRESTORE)
}