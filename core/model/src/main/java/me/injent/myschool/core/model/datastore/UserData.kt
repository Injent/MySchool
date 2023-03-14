package me.injent.myschool.core.model.datastore

import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.model.ReportingPeriod
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.model.UserContext

data class UserData(
    val userContext: UserContext? = null,
    val lastSyncTime: LocalDateTime? = null,
    val bannedSubjects: List<Subject> = emptyList(),
    val period: ReportingPeriod? = null,
    val isInitialized: Boolean = false
)