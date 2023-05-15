package me.injent.myschool.feature.accounts.model

data class UserAccount(
    val userId: Long,
    val name: String,
    val avatarUrl: String? = null
)
