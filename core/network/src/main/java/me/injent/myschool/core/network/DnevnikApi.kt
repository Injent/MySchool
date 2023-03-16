package me.injent.myschool.core.network

object DnevnikApi {
    const val BASE_URL = "https://api.dnevnik.ru"
    const val USER_ID = "userId"
    const val EDUGROUP_ID = "eduGroupId"
    const val PERSON_ID = "personId"
    const val SUBJECT_ID = "subject"
    const val FROM_PERIOD = "from"
    const val TO_PERIOD = "to"
    const val PERIOD_ID = "periodId"
    const val SCHOOL_ID = "schoolId"
    const val LESSON_ID = "lessonId"

    const val CHECK_TOKEN_EXPIRATION = "users/me/organizations"
    const val CONTEXT = "users/me/context"
    const val PERSON = "users/{$USER_ID}"
    const val PEOPLE_IN_EDUGROUP = "v2/edu-groups/{$EDUGROUP_ID}/persons"
    const val REPORTING_PERIODS = "edu-groups/{$EDUGROUP_ID}/reporting-periods"
    const val CLASSMATES = ""

    const val PERSON_MARK_BY_PERIOD_AND_SUBJECT = "v2/persons/{$PERSON_ID}/subjects/{$SUBJECT_ID}/marks/{$FROM_PERIOD}/{$TO_PERIOD}"
    const val PERSON_MARKS_BY_PERIOD = "/v2/persons/{$PERSON_ID}/schools/{$SCHOOL_ID}/marks/{$FROM_PERIOD}/{$TO_PERIOD}"
    const val SUBJECTS = "v2/edu-groups/{$EDUGROUP_ID}/subjects"
    const val AVERAGE_MARK = "v2/persons/{$PERSON_ID}/reporting-periods/{$PERIOD_ID}/avg-mark"
    const val EDUGROUP_MARKS = "v2/edu-groups/{$EDUGROUP_ID}/subjects/{$SUBJECT_ID}/marks/{$FROM_PERIOD}/{$TO_PERIOD}"

    const val LESSON = ""
    const val HOMEWORKS = "v2/users/me/school/{$SCHOOL_ID}/homeworks"
}