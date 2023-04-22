package me.injent.myschool.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthState
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
) : ViewModel() {

    private val _accountsUiState = MutableStateFlow(AccountsUiState())
    val accountsUiState: StateFlow<AccountsUiState>
        get() = _accountsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            workStatusMonitor.isSyncing
                .onEach { syncState ->
                    when (syncState) {
                        SyncState.IDLE -> setAuthState(AuthState.NotAuthed)
                        SyncState.SYNCING -> setAuthState(AuthState.Loading)
                        SyncState.SUCCESS -> {
                            setAuthState(AuthState.Success)
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
        _accountsUiState.update {
            it.copy(
                isLoading = false,
                accounts = accounts
            )
        }
    }

    fun selectAccount(account: ExpandedAccount) {
        if (_accountsUiState.value.authState is AuthState.Loading) return
        val isSameAccount = accountHelper.getCurrentAccount()?.name?.toLong() == account.userId
        if (isSameAccount) {
            setAuthState(AuthState.Success)
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

    private fun setAuthState(authState: AuthState) {
        _accountsUiState.update {
            it.copy(authState = authState)
        }
    }
}

data class AccountsUiState(
    val isLoading: Boolean = true,
    val accounts: List<ExpandedAccount> = emptyList(),
    val authState: AuthState = AuthState.NotAuthed
)
