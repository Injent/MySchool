package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class Mark(
    val id: Long,
    val value: String,
    val date: LocalDateTime,
    val personId: Long,
    val workId: Long,
    val lessonId: Long?,
    val mood: Mood,
    val dbSubjectId: Long? = null
) {
    enum class Mood {
        Good,
        Average,
        Bad,
        NotSet
    }
}

data class PersonAndMarkValue(
    val personId: Long,
    val personName: String,
    val avatarUrl: String?,
    val value: Float
)