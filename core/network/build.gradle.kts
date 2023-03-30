plugins {
    id("injent.android.library")
    id("injent.android.hilt")
    id("injent.kotlin.serialization")
}

android {
    namespace = "me.injent.myschool.core.network"

    defaultConfig {
        buildConfigField(
            "String",
            "API_BASE_URL",
            "\"https://api.dnevnik.ru/v2/authorizations/bycredentials\""
        )
        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"0925b3b0d1e84c05b85851e4f8a4033d\""
        )
        buildConfigField(
            "String",
            "CLIENT_SECRET",
            "\"3771967e6bf140359ab60a8894106947\""
        )
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(Dependencies.DATETIME)
    retrofit()
}