package me.injent.myschool.core.domain.model

data class PersonWithAverageMark(
    val personId: Long,
    val personName: String,
    val avatarUrl: String?,
    val averageMarkValue: Float
)