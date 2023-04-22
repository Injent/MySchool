package me.injent.myschool.auth

sealed interface AuthState {
    data class Error(val message: String? = null) : AuthState
    object Success : AuthState
    object HaveMultipleAccounts : AuthState
    object NotAuthed : AuthState
    object Loading : AuthState
    object NotStudent : AuthState
    object CheckingAccounts : AuthState
}