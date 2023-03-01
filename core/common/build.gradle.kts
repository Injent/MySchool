plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.common"
}

dependencies {
    implementation(Dependencies.SECURITY_CRYPTO)
}