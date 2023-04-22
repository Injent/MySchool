plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.data"
}

dependencies {
    implementation(project(":core:analytics"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(Dependencies.DATETIME)
    implementation(Dependencies.FIREBASE_BOM)
    implementation(Dependencies.FIREBASE_FIRESTORE)
}