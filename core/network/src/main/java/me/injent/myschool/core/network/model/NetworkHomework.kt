package me.injent.myschool.core.network.model

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.Homework

@Serializable
data class NetworkHomework(
    val text: String,
    val subjectId: Long,
    val files: List<Long>,
    val lesson: Long,
    val sentDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)