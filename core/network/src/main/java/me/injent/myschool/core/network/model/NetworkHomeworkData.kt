package me.injent.myschool.core.network.model

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.model.Homework

@Serializable
data class NetworkHomeworkData(
    @SerialName("works")
    val homeworks: List<NetworkHomework>,
    val files: List<NetworkAttachment>,
    val subjects: List<NetworkSubject>,
    val lessons: List<Lesson>,
    val teachers: List<NetworkTeacher>
) {
    @Serializable
    data class Lesson(
        val id: Long,
        val teachers: List<Long>
    )
}

fun NetworkHomeworkData.asExternalModelList(): List<Homework> {
    return homeworks
        .map { homework ->
            val subject = subjects.find { it.id == homework.subjectId }!!
            val files = this.files.filter { homework.files.contains(it.id) }
            val lesson = this.lessons.find { it.id == homework.lesson }!!
            val teacher = this.teachers.find { it.id == lesson.teachers.first() }!!
            Homework(
                text = homework.text,
                subject = subject.asExternalModel(),
                attachments = files.map(NetworkAttachment::asExternalModel),
                sentDate = homework.sentDate.atTimeZone(TimeZone.currentSystemDefault()),
                teacher = teacher.asExternalModel()
            )
        }
}