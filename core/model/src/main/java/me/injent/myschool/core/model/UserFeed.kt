package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class UserFeed(
    val days: List<Day>
) {
    val currentDay: Day?
        get() = days.firstOrNull()

    data class Day(
        val date: LocalDateTime,
        val nextWorkingDayDate: LocalDateTime?,
        val marksCards: List<MarkCard>,
        val todayHomeworks: List<Homework>,
        val todaySchedule: List<Schedule>
    )

    data class MarkCard(
        val subjectName: String,
        val subjectId: Long,
        val workTypeName: String,
        val lesson: Lesson?,
        val marks: List<Mark>
    )

    data class Homework(
        val subjectName: String,
        val workTypeName: String,
        val work: Work
    )

    data class Lesson(
        val id: Long,
        val date: LocalDateTime,
    )

    data class Schedule(
        val lessonId: Long,
        val lessonStatus: String,
        val number: Int,
        val subjectName: String
    )
}
