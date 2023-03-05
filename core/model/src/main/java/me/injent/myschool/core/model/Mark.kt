package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class Mark(
    val id: Long,
    val value: String,
    val date: LocalDateTime,
    val personId: Long,
    val workId: Long,
    val lessonId: Long,
    val dbSubjectId: Long? = null
)

data class PersonAndMarkValue(
    val personId: Long,
    val personName: String,
    val value: Float
)