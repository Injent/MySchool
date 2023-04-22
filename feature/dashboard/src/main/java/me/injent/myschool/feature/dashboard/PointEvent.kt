package me.injent.myschool.feature.dashboard

import me.injent.myschool.core.model.Schedule

sealed interface PointEvent {
    data class GoToMarkDetails(val markId: Long) : PointEvent
    data class OpenLessonDialog(val homework: Schedule.Lesson) : PointEvent
    data class ChangeScheduleVariant(val variant: Schedule.Variant) : PointEvent
    object CloseLessonDialog : PointEvent
    object BackEvent : PointEvent
    object RetryRecentMarks : PointEvent
}