package me.injent.myschool.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.network.EpochLocalDateSerializer

//@Serializable
//data class NetworkFeedSchedule(
//    @Serializable(EpochLocalDateSerializer::class)
//    val nextLessonDate: LocalDate,
//    val todayLessons: List<FeedLesson>,
//    val recentMarks: List<RecentMark>
//) {
//    @Serializable
//    data class FeedLesson(
//        val comment: String,
//        val endTime: LocalDateTime
//    )
//}
