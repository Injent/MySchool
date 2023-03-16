package me.injent.myschool.feature.authorization

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.injent.myschool.core.common.SessionManager
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.core.data.util.SyncStatusMonitor
import me.injent.myschool.sync.initializers.showMarkUpdateNotification
import me.injent.myschool.sync.startOneTimeSyncWork
import me.injent.myschool.sync.startPeriodicMarkUpdateWork
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    syncStatusMonitor: SyncStatusMonitor,
    workManager: WorkManager
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

    init {
        if (token != null) {
            viewModelScope.launch {
                SessionManager(context).saveToken(token)
                delay(100)
                workManager.startOneTimeSyncWork()
            }
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}