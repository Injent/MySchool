package me.injent.myschool.feature.authorization

enum class AuthState {
    NETWORK_ERROR,
    SUCCESS,
    NOT_AUTHED,
    LOADING,
    CHECKING_TOKEN
}