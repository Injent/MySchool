package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.UserContext

@Serializable
data class ContextPersonResponse(
    val contextPersons: List<UserContext>
) {
    fun getUserContext() = contextPersons.first()
}