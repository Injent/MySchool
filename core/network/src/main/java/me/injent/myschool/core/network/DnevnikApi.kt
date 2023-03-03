package me.injent.myschool.core.network

object DnevnikApi {
    const val BASE_URL = "https://api.dnevnik.ru/v2/"
    const val USER_ID = "userId"
    const val EDUGROUP_ID = "eduGroupId"

    const val CHECK_TOKEN_EXPIRATION = "users/me/organizations"
    const val CONTEXT = "users/me/context"
    const val PERSON = "users/{$USER_ID}"
    const val PEOPLE_IN_EDUGROUP = "edu-groups/{$EDUGROUP_ID}/persons"
    const val REPORTING_PERIODS = "edu-groups/{$EDUGROUP_ID}/reporting-periods"
    const val CLASSMATES = "users/me/classmates"
}