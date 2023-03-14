package me.injent.myschool.core.model

import kotlinx.datetime.*

data class Homework(
    val text: String,
    val subject: Subject,
    val files: List<Attachment>,
    val teacher: Teacher,
    val sentDate: LocalDateTime
)