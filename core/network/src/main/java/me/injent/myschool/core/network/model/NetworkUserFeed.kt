package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EpochLocalDateTimeSerializer
import me.injent.myschool.core.model.UserFeed

@Serializable
data class NetworkUserFeed(
    val schedule: Schedule,
    val recentMarks: List<RecentMark>,
    val feed: List<FeedItem>
) {
    @Serializable
    data class Schedule(
        val todayLessons: List<Lesson>
    )

    @Serializable
    data class Lesson(
        val id: Long,
        val number: Int,
        val place: String,
        val hours: NetworkHours,
        val startTime: Long,
        val endTime: Long,
        val subject: Subject
    )

    @Serializable
    data class Subject(
        val id: Long,
        val name: String
    )

    @Serializable
    data class RecentMark(
        @Serializable(EpochLocalDateTimeSerializer::class)
        val date: LocalDateTime,
        @Serializable(EpochLocalDateTimeSerializer::class)
        val lessonDate: LocalDateTime? = null,
        val subject: Subject,
        val marks: List<Mark>,
        val markTypeText: String
    )

    @Serializable
    data class Mark(
        val id: Long,
        val value: String,
        @Serializable
        val mood: me.injent.myschool.core.model.Mark.Mood
    )

    @Serializable
    data class FeedItem(
        val type: Type,
        val content: Content
    ) {
        @Serializable
        enum class Type {
            StudentRating,
            Post,
            WeekSummary,
            ImportantWork
        }

        @Serializable
        data class Content(
            @Serializable(EpochLocalDateTimeSerializer::class)
            val date: LocalDateTime? = null,
            val items: List<Item>? = null
        ) {
            @Serializable
            data class Item(
                @Serializable(EpochLocalDateTimeSerializer::class)
                val date: LocalDateTime,
                val subject: Subject,
                val subjectMarks: List<SubjectMark>
            )

            @Serializable
            data class Subject(
                val id: Long,
                val name: String
            )

            @Serializable
            data class SubjectMark(
                val marks: List<Mark>
            )
        }
    }
}

fun NetworkUserFeed.asExternalModel(): UserFeed {
    val currentSeconds = java.time.Clock.systemUTC().instant().epochSecond

    return UserFeed(
        currentLesson = schedule.todayLessons.find { lesson ->
            currentSeconds in lesson.startTime..lesson.endTime
        }?.asExternalModel(),
        recentMarks = recentMarks.map(NetworkUserFeed.RecentMark::asExternalModel),
        weekSummary = feed.filter {
            it.type == NetworkUserFeed.FeedItem.Type.WeekSummary
        }.map(NetworkUserFeed.FeedItem::asSubjectCard),
        posts = emptyList()
    )
}

fun NetworkUserFeed.Lesson.asExternalModel() = UserFeed.Lesson(
    id = id,
    number = number,
    place = place,
    hours = hours.asExternalModel(),
    subjectName = subject.name,
    startTime = startTime,
    endTime = endTime
)

fun NetworkUserFeed.RecentMark.asExternalModel() = UserFeed.RecentMark(
    date = date,
    lessonDate = lessonDate,
    subjectName = subject.id.toString(),
    marks = marks.map { mark ->
        with(mark) {
            UserFeed.Mark(
                id = id,
                value = value,
                mood = mood
            )
        }
    },
    markTypeText = markTypeText
)

fun NetworkUserFeed.FeedItem.asSubjectCard(): UserFeed.SubjectCard {
    if (this.type != NetworkUserFeed.FeedItem.Type.WeekSummary) throw IllegalStateException()
    return UserFeed.SubjectCard(
        date = this.content.date!!,
        subjectName = content.items?.first()!!.subject.name,
        marks = content.items.first().subjectMarks
            .map(NetworkUserFeed.FeedItem.Content.SubjectMark::asExternalModel)
    )
}

fun NetworkUserFeed.FeedItem.Content.SubjectMark.asExternalModel() = UserFeed.Mark(
    id = this.marks.first().id,
    value = this.marks.first().value,
    mood = this.marks.first().mood
)