package me.injent.myschool.core.network

object DnevnikApi {
    const val BASE_URL = "https://api.dnevnik.ru/"
    const val USER_ID = "userId"
    const val EDUGROUP_ID = "eduGroupId"

    object V2 {
        const val BASE = "v2"
        const val CHECK_TOKEN_EXPIRATION = "$BASE/users/me/organizations"
        const val CONTEXT = "$BASE/users/me/context"
        const val EXTERNAL_USER_PROFILE = "$BASE/users/{$USER_ID}"
        const val PEOPLE_IN_EDUGROUP = "$BASE/edu-groups/{$EDUGROUP_ID}/persons"
        const val REPORTING_PERIODS = "$BASE/edu-groups/{$EDUGROUP_ID}/reporting-periods"
    }
}