package me.injent.myschool.auth

sealed interface AuthStatus {
    data class Error(val message: String? = null) : AuthStatus
    object Success : AuthStatus
    object HaveMultipleAccounts : AuthStatus
    object NotAuthed : AuthStatus
    object Loading : AuthStatus
    object Connecting : AuthStatus
    object Offline : AuthStatus
    object CheckingAccounts : AuthStatus
}