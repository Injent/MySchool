package me.injent.myschool.feature.auth

import androidx.lifecycle.SavedStateHandle
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
import me.injent.myschool.core.data.util.SyncProgressMonitor
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.feature.auth.navigation.ARG_TOKEN
import me.injent.myschool.sync.WorkHelper
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountHelper: AccountHelper,
    private val loginRepository: LoginRepository,
    private val userContextRepository: UserContextRepository,
    private val syncStatusMonitor: SyncStatusMonitor,
    syncProgressMonitor: SyncProgressMonitor,
    private val workHelper: WorkHelper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val token: String? = savedStateHandle[ARG_TOKEN]

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.NotAuthed)
    val authState: StateFlow<AuthState>
        get() = _authState.asStateFlow()

    val syncProgress = syncProgressMonitor.progress
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = 0
        )

    init {
        viewModelScope.launch {
            syncStatusMonitor.isSyncing
                .onEach { state ->
                    if (state == SyncState.SUCCESS) {
                        successLogin()
                    }
                }
                .collect()
        }
        viewModelScope.launch {
            startSync(token)
        }
    }

    private suspend fun createAccount(accessToken: String) {
        val userContext = loginRepository.login(accessToken)

        userContextRepository.setUserContext(userContext)
        accountHelper.updateAccount(accessToken, userContext)
    }

    private suspend fun startSync(accessToken: String?) {
        if (accessToken != null) {
            _authState.value = AuthState.Loading
            createAccount(accessToken)
            workHelper.startOneTimeSyncWork()
        }
    }

    private fun successLogin() {
        _authState.value = AuthState.Success
        workHelper.pruneWork()
        workHelper.startPeriodicMarkUpdateWork()
    }
}