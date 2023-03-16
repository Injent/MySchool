package me.injent.myschool.core.model

import kotlinx.serialization.Serializable

/**
 * The main model that contains data with which other data can be received
 */
@Serializable
data class UserContext(
    val userId: Long,
    val personId: Long,
    val avatarUrl: String? = null,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val sex: Sex,
    val group: Group,
    val reportingPeriodGroup: ReportingPeriodGroup,
    val school: School,
)