plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.sync"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(Dependencies.WORK)
    implementation(Dependencies.HILT_WORK)
    implementation(Dependencies.STARTUP)
    implementation(Dependencies.COROUTINES)
    implementation(Dependencies.LIVEDATA)
}