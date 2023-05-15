package me.injent.myschool.feature.accounts

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
import me.injent.myschool.core.ui.util.BaseViewModel
import me.injent.myschool.feature.accounts.model.UserAccount
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
) : BaseViewModel<UiState, Event, Action>() {

    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

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

    override fun processAction(action: Action) = when (action) {
        Action.Back -> sendEvent(Event.Back)
        is Action.RemoveAccount -> TODO()
        is Action.SelectAccount -> selectAccount(action.account)
    }

    private fun loadAccounts() {
        val accounts = accountHelper.accounts
            .map { androidAccount ->
                UserAccount(
                    userId = androidAccount.name.toLong(),
                    name = accountHelper.getAccountUserName(androidAccount),
                    avatarUrl = accountHelper.getAccountAvatarUrl(androidAccount)
                )
            }
        _uiState.update {
            it.copy(
                isLoading = false,
                accounts = accounts
            )
        }
    }

    fun selectAccount(account: UserAccount) {
        if (_uiState.value.authStatus is AuthStatus.Loading) return
        val isSameAccount = accountHelper.getCurrentAccount()?.name?.toLong() == account.userId
        if (isSameAccount) {
            setAuthState(AuthStatus.Success)
            return
        }

        viewModelScope.launch {
            val androidAccount = accountHelper.accounts
                .find { it.name.toLong() == account.userId }!!
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
        _uiState.update { it.copy(authStatus = authStatus) }
    }
}