package me.injent.myschool.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.core.data.repository.LoginRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.feature.accounts.model.ExpandedAccount
import me.injent.myschool.sync.WorkHelper
import me.injent.myschool.sync.monitor.SyncWorkStatusMonitor
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountHelper: AccountHelper,
    private val loginRepository: LoginRepository,
    private val workHelper: WorkHelper,
    private val userContextRepository: UserContextRepository,
    private val userDataRepository: UserDataRepository,
    workStatusMonitor: SyncWorkStatusMonitor
) : ViewModel(), AccountsContract {

    private val _state = MutableStateFlow(AccountsContract.State())
    override val state: StateFlow<AccountsContract.State>
        get() = _state.asStateFlow()

    override fun onEvent(event: AccountsContract.Event) {
        when (event) {
            AccountsContract.Event.AddAccount -> TODO()
            is AccountsContract.Event.DeleteAccount -> TODO()
            is AccountsContract.Event.SelectAccount -> TODO()
        }
    }

    init {
        viewModelScope.launch {
            workStatusMonitor.isSyncing
                .onEach { syncState ->
                    when (syncState) {
                        SyncState.IDLE -> setAuthState(AuthStatus.NotAuthed)
                        SyncState.SYNCING -> setAuthState(AuthStatus.Loading)
                        SyncState.SUCCESS -> {
                            setAuthState(AuthStatus.Success)
                            workHelper.pruneWork()
                        }
                    }
                }
                .collect()
        }
        loadAccounts()
    }

    private fun loadAccounts() {
        val accounts = accountHelper.accounts
            .map { androidAccount ->
                ExpandedAccount(
                    userId = androidAccount.name.toLong(),
                    name = accountHelper.getAccountUserName(androidAccount),
                    avatarUrl = accountHelper.getAccountAvatarUrl(androidAccount)
                )
            }
        _state.update {
            it.copy(
                isLoading = false,
                accounts = accounts
            )
        }
    }

    fun selectAccount(account: ExpandedAccount) {
        if (_state.value.status is AuthStatus.Loading) return
        val isSameAccount = accountHelper.getCurrentAccount()?.name?.toLong() == account.userId
        if (isSameAccount) {
            setAuthState(AuthStatus.Success)
            return
        }

        viewModelScope.launch {
            val androidAccount =
                accountHelper.accounts.find { it.name.toLong() == account.userId }!!
            val accessToken = accountHelper.getAccountAuthToken(androidAccount)
            val userContext = loginRepository.login(accessToken)
            userDataRepository.clear()
            userContextRepository.setUserContext(userContext)

            accountHelper.updateAccount(
                accessToken,
                userContext
            )
            workHelper.startOneTimeSyncWork()
        }
    }

    private fun setAuthState(authStatus: AuthStatus) {
        _state.update { it.copy(status = authStatus) }
    }
}