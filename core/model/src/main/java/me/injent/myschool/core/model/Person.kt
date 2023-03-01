package me.injent.myschool.core.model

import kotlinx.datetime.Instant

/**
 * Uses for viewing other user profile page
 */
data class Person(
    val id: Long,
    val shortName: String,
    val locale: String,
    val birthday: Instant? = null,
    val sex: String,
    val roles: List<String>,
    val phone: String? = null
)
