package me.injent.myschool.feature.authorization

sealed interface PointEvent {
    object Login : PointEvent
    data class LoginTextChange(val value: String) : PointEvent
    data class PasswordTextChange(val value: String) : PointEvent
}