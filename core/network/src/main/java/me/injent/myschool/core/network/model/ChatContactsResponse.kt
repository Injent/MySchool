package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatContactsResponse(
    val contacts: List<Contact>
) {
    @Serializable
    data class Contact(
        val jid: String,
        val name: String,
        val avatar: String?
    ) {
        val userId: Long
            get() = try {
                jid.substringAfter("user_").substringBefore("@").toLong()
            } catch (_: Exception) {
                0
            }
    }
}
