package me.injent.myschool.core.model

import kotlinx.datetime.LocalDate

/**
 * Uses for viewing other user profile page
 */
data class Person(
    val id: Long,
    val personId: Long,
    val shortName: String,
    val locale: String,
    val birthday: LocalDate? = null,
    val sex: Sex,
    val roles: List<String>,
    val phone: String? = null
)

enum class Sex(val serializeName: String) {
    MALE("Male"),
    FEMALE("Female")
}