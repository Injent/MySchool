package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.network.DaySerializer

@Serializable
data class NetworkUserFeed(
    val days: List<Day>
) {
    @Serializable(DaySerializer::class)
    data class Day(
        val date: LocalDateTime,
        val nextWorkingDayDate: LocalDateTime?,
        val marksCards: List<MarkCard>,
        val todayHomeworks: List<Homework>,
        val todaySchedule: List<Schedule>
    )

    @Serializable
    data class MarkCard(
        val subjectName: String,
        val subjectId: Long,
        val workTypeName: String,
        val lesson: Lesson?,
        val marks: List<NetworkMark>,
    )

    @Serializable
    data class Homework(
        val subjectName: String,
        val workTypeName: String,
        val work: NetworkWork
    )

    @Serializable
    data class Lesson(
        val id: Long,
        val date: LocalDateTime,
    )

    @Serializable
    data class Schedule(
        val lessonId: Long,
        val lessonStatus: String,
        val number: Int,
        val subjectName: String
    )
}

fun NetworkUserFeed.asExternalModel() = UserFeed(
    days = days.map { day ->
        with(day) {
            UserFeed.Day(
                date = date,
                nextWorkingDayDate = nextWorkingDayDate,
                marksCards = marksCards
                    .map { markCard ->
                        with(markCard) {
                            UserFeed.MarkCard(
                                subjectName = subjectName,
                                subjectId = subjectId,
                                workTypeName = workTypeName,
                                lesson = lesson?.run { UserFeed.Lesson(id, date) },
                                marks = marks.map(NetworkMark::asExternalModel)
                            )
                        }
                    },
                todayHomeworks = todayHomeworks
                    .map { homework ->
                        with(homework) {
                            UserFeed.Homework(
                                subjectName = subjectName,
                                workTypeName = workTypeName,
                                work = work.asExternalModel()
                            )
                        }
                    },
                todaySchedule = todaySchedule
                    .map { schedule ->
                        with(schedule) {
                            UserFeed.Schedule(
                                lessonId = lessonId,
                                lessonStatus = lessonStatus,
                                number = number,
                                subjectName = subjectName
                            )
                        }
                    }
            )
        }
    }
)