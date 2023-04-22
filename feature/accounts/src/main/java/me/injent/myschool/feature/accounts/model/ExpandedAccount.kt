package me.injent.myschool.feature.accounts.model

data class ExpandedAccount(
    val userId: Long,
    val name: String,
    val avatarUrl: String? = null
)
