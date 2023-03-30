package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.RecentMarks

@Serializable
data class RecentMarksResponse(
    val marks: List<NetworkMark>,
    val subjects: List<NetworkSubject>,
    val works: List<NetworkWork>,
    val workTypes: List<NetworkWorkType>,
    val lessons: List<Lesson>
) {
    @Serializable
    data class Lesson(
        val id: Long,
        val title: String,
        val date: LocalDateTime,
        val hours: String,
        val works: List<Long>,
        val teachers: List<Long>,
        val status: String,
        val number: Int
    )
}

fun RecentMarksResponse.asExternalModel() = RecentMarks(
    subjectToMarks = subjectToMarks()
)

private fun RecentMarksResponse.subjectToMarks() =
    subjects.associate { subject ->
        val work = works.find { it.subjectId == subject.id }!!
        val marks = marks.filter { it.lessonId == work.lessonId }
        Pair(subject.asExternalModel(), marks.map(NetworkMark::asExternalModel))
    }