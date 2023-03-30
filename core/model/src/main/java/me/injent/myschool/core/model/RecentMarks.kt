package me.injent.myschool.core.model

data class RecentMarks(
    val subjectToMarks: Map<Subject, List<Mark>>
)
