package me.injent.myschool.core.model

/**
 * The main model that contains data with which other data can be received
 */
data class UserContext(
    val userId: Long,
    val personId: Long,
    val shortName: String,
    val school: School,
    val eduGroup: EduGroup
)