package me.injent.myschool.feature.accounts

import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.common.util.UnidirectionalViewModel
import me.injent.myschool.feature.accounts.model.ExpandedAccount

interface AccountsContract
    : UnidirectionalViewModel<AccountsContract.State, AccountsContract.Event> {

    data class State(
        val isLoading: Boolean = true,
        val accounts: List<ExpandedAccount> = emptyList(),
        val status: AuthStatus = AuthStatus.NotAuthed
    )

    sealed class Event {
        data class DeleteAccount(val userId: Long) : Event()
        data class SelectAccount(val userId: Long) : Event()
        object AddAccount : Event()
    }
}