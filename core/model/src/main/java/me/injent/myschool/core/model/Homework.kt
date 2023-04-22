package me.injent.myschool.core.model

import kotlinx.datetime.LocalDateTime

data class Homework(
    val text: String,
    val subject: Subject,
    val attachments: List<Attachment>,
    val teacher: Teacher,
    val sentDate: LocalDateTime
)