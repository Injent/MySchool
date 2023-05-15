package me.injent.myschool.feature.auth

import android.content.Context
import me.injent.myschool.auth.AuthStatus

data class UiState(
    val login: String = "",
    val password: String = "",
    val authStatus: AuthStatus = AuthStatus.NotAuthed
)

sealed interface UiEvent {
    object Back : UiEvent
}

sealed interface Action {
    object Back : Action
    data class Login(val context: Context) : Action
    data class ChangeLogin(val value: String) : Action
    data class ChangePassword(val value: String) : Action
}