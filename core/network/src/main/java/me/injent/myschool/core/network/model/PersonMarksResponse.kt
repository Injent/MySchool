package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonMarksResponse(
    val periodMarks: List<String>,
    val periodNumber: Int,
)
