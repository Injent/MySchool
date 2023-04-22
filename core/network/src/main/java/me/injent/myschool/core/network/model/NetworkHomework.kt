package me.injent.myschool.core.network.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHomework(
    val text: String,
    val subjectId: Long,
    val files: List<Long>,
    val lesson: Long,
    val sentDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val targetDate: LocalDateTime?
)