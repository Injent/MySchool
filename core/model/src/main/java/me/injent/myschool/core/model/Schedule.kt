package me.injent.myschool.core.model

data class Schedule(
    val lessons: List<Lesson>
) {
    data class Lesson(
        val id: Long,
        val number: Int,
        val place: String,
        val hours: String,
        val theme: String,
        val subjectName: String,
        val homeworkText: String?,
        val attachments: List<Attachment>,
        val hasAttachment: Boolean,
        val teacherName: String
    )

    enum class Variant {
        Today,
        Tomorrow
    }
}