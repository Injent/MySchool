package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class UserFeed(
    val currentLesson: Lesson?,
    val recentMarks: List<RecentMark>,
    val weekSummary: List<SubjectCard>,
    val posts: List<Post>
) {
    data class Lesson(
        val id: Long,
        val number: Int,
        val place: String,
        val hours: Hours,
        val startTime: Long,
        val endTime: Long,
        val subjectName: String
    )

    data class RecentMark(
        val date: LocalDateTime,
        val lessonDate: LocalDateTime?,
        val subjectName: String,
        val marks: List<Mark>,
        val markTypeText: String
    )

    data class Mark(
        val id: Long,
        val value: String,
        val mood: me.injent.myschool.core.model.Mark.Mood
    )

    data class SubjectCard(
        val date: LocalDateTime,
        val subjectName: String,
        val marks: List<Mark>
    )

    data class Post(
        val title: String?,
        val subtitle: String?,
        val text: String?,
        val createdDateTime: LocalDateTime,
        val commentsCount: Int,
        val authorImageUrl: String?,
        val authorFirstName: String?,
        val authorMiddleName: String?,
        val authorLastName: String,
        val files: List<File>
    ) {
        data class File(
            val fileName: String,
            val fileLink: String
        )
    }
}
