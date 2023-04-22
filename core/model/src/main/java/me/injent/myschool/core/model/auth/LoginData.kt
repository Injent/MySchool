package me.injent.myschool.core.model.auth

data class LoginData(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String,
    val scope: String,
    val userId: Long
)