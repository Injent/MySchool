package me.injent.myschool.feature.authorization

enum class AuthState {
    TOKEN_EXPIRED,
    SUCCESS,
    NOT_AUTHED,
    LOADING,
    CHECKING_TOKEN
}