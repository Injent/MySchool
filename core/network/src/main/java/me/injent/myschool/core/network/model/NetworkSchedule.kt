package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochLocalDateTimeSerializer
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.model.Schedule

@Serializable
data class NetworkSchedule(
    val days: List<Day>
) {
    @Serializable
    data class Day(
        @Serializable(EpochLocalDateTimeSerializer::class)
        val date: LocalDateTime,
        val lessons: List<Lesson>
    )

    @Serializable
    data class Lesson(
        val id: Long,
        val number: Int,
        val place: String?,
        val hours: NetworkHours,
        val theme: String?,
        val subject: NetworkSubject?,
        val homework: Homework?,
        val hasAttachment: Boolean
    )

    @Serializable
    data class Homework(
        val text: String
    ) {
        val formatText: String
            get() = text.split(";").joinToString("\n•", prefix = "• ")
    }
}

fun NetworkSchedule.asExternalModel() = Schedule(
    lessons = days.first().lessons.map(NetworkSchedule.Lesson::asExternalModel)
)

fun NetworkSchedule.Lesson.asExternalModel() = Schedule.Lesson(
    id = id,
    number = number,
    place = place ?: "",
    hours = hours.interval,
    theme = theme ?: "",
    subjectName = subject?.name ?: "",
    homeworkText = homework?.formatText ?: "",
    attachments = emptyList(),
    teacherName = "",
    hasAttachment = hasAttachment
)