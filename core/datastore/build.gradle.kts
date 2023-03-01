plugins {
    id("injent.android.library")
    id("injent.android.hilt")
    id("injent.kotlin.serialization")
}

android {
    namespace = "me.injent.myschool.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(Dependencies.COROUTINES)
    implementation(Dependencies.DATASTORE)
}