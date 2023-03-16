package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkLesson(
    val id: Long,
    val title: String,
    val teachers: List<Long>,
    val subject: NetworkSubject? = null
)
