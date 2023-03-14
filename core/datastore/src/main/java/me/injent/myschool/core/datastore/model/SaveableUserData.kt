package me.injent.myschool.core.datastore.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.datastore.UserData

@Serializable
data class SaveableUserData(
    val userContext: SaveableUserContext? = null,
    val lastSyncTime: LocalDateTime? = null,
    val bannedSubjects: List<SaveableSubject> = emptyList(),
    val period: SaveableReportingPeriod? = null,
    val isInitialized: Boolean = false
)

fun SaveableUserData.asExternalModel() = UserData(
    userContext = userContext?.asExternalModel(),
    lastSyncTime = lastSyncTime,
    bannedSubjects = bannedSubjects.map(SaveableSubject::asExternalModel),
    period = period?.asExternalModel(),
    isInitialized = isInitialized
)