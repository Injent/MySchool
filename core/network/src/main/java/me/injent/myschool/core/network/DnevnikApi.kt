package me.injent.myschool.core.network

object DnevnikApi {
    const val BASE_URL = "https://api.dnevnik.ru/v2/"
    const val USER_ID = "userId"
    const val EDUGROUP_ID = "eduGroupId"
    const val PERSON_ID = "personId"
    const val SUBJECT_ID = "subject"
    const val FROM_PERIOD = "from"
    const val TO_PERIOD = "to"
    const val PERIOD_ID = "periodId"

    const val CHECK_TOKEN_EXPIRATION = "users/me/organizations"
    const val CONTEXT = "users/me/context"
    const val PERSON = "users/{$USER_ID}"
    const val PEOPLE_IN_EDUGROUP = "edu-groups/{$EDUGROUP_ID}/persons"
    const val REPORTING_PERIODS = "edu-groups/{$EDUGROUP_ID}/reporting-periods"
    const val CLASSMATES = "users/me/classmates"

    const val PERSON_MARK_BY_PERIOD = "persons/{$PERSON_ID}/subjects/{$SUBJECT_ID}/marks/{$FROM_PERIOD}/{$TO_PERIOD}"
    const val SUBJECTS = "edu-groups/{$EDUGROUP_ID}/subjects"
    const val AVERAGE_MARK = "persons/{$PERSON_ID}/reporting-periods/{$PERIOD_ID}/avg-mark"
    const val EDUGROUP_MARKS = "edu-groups/{$EDUGROUP_ID}/subjects/{$SUBJECT_ID}/marks/{$FROM_PERIOD}/{$TO_PERIOD}"
}