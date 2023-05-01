plugins {
    id("injent.android.library")
    id("injent.android.hilt")
}

android {
    namespace = "me.injent.myschool.core.auth"

    defaultConfig {
        val url = "https://login.dnevnik.ru/oauth2?response_type=token" +
                "&client_id=bb97b3e445a340b9b9cab4b9ea0dbd6f" +
                "&scope=CommonInfo,ContactInfo,FriendsAndRelatives,EducationalInfo" +
                "&redirect_uri=https://myauth"

        buildConfigField("String", "AUTH_URL", "\"$url\"")
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(Dependencies.SECURITY_CRYPTO)
    implementation(Dependencies.DATETIME)
}