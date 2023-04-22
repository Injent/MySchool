package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class Work(
    val id: Long,
    val lessonId: Long,
    val text: String,
    val subjectId: Long,
    val targetDate: LocalDateTime,
    val sentDate: LocalDateTime?,
    val status: String,
    val type: String
) {
    enum class Status {
        New,
        Sent
    }
    enum class Type {
        PeriodMark,
        CommonWork,
        LaboratoryWork,
        DefaultNewLessonWork,
        Homework
    }
}
