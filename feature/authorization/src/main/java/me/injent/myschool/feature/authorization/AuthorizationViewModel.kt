package me.injent.myschool.feature.authorization

import android.accounts.AccountManager
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.core.common.SessionManager
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.core.data.repository.remote.LoginRepository
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.sync.startOneTimeSyncWork
import me.injent.myschool.sync.startPeriodicMarkUpdateWork
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    syncStatusMonitor: SyncStatusMonitor,
    workManager: WorkManager,
    loginRepository: LoginRepository
) : ViewModel() {

    private val token: String? = savedStateHandle[TOKEN]

    val authState = syncStatusMonitor.isSyncing
        .map {
            when (it) {
                SyncState.IDLE -> AuthState.NOT_AUTHED
                SyncState.SYNCING -> AuthState.LOADING
                SyncState.SUCCESS -> {
                    workManager.startPeriodicMarkUpdateWork()
                    AuthState.SUCCESS
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthState.CHECKING_TOKEN
        )

    private val _login = MutableStateFlow("")
    val login: StateFlow<String>
        get() = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String>
        get() = _password.asStateFlow()

    init {
        val am = AccountManager.get(context)
        if (token != null) {
            viewModelScope.launch {
                SessionManager(context).saveToken(token)
                delay(100)
                workManager.startOneTimeSyncWork()
            }
        }
    }

    fun onEvent(event: PointEvent) {
        when (event) {
            PointEvent.Login -> {

            }
            is PointEvent.LoginTextChange -> {

            }
            is PointEvent.PasswordTextChange -> {

            }
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}