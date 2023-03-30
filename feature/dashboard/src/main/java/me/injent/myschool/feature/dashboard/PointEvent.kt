package me.injent.myschool.feature.dashboard

import me.injent.myschool.core.model.UserFeed

sealed interface PointEvent {
    data class GoToMarkDetails(val markId: Long) : PointEvent
    data class OpenHomeworkDialog(val homework: UserFeed.Homework) : PointEvent
    object CloseHomeworkDialog : PointEvent
    object BackEvent : PointEvent
}