package me.injent.myschool.feature.auth

import android.content.Context
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.common.util.UnidirectionalViewModel

interface LoginContract
    : UnidirectionalViewModel<LoginContract.State, LoginContract.Event> {

    data class State(
        val status: AuthStatus = AuthStatus.NotAuthed,
        val login: String = "",
        val password: String = "",
        val readyToLogin: Boolean = false
    )

    sealed class Event {
        data class OnLoginChange(val value: String) : Event()
        data class OnPasswordChange(val value: String) : Event()
        data class OnLogin(val context: Context) : Event()
    }
}