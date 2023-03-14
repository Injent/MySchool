package me.injent.myschool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import me.injent.myschool.core.common.SessionManager
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.feature.authorization.AuthState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    sessionManager: SessionManager,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val authState: StateFlow<AuthState> = combine(
        networkMonitor.isOnline, sessionManager.isTokenActive
    ) { isOnline, isTokenActive ->
        val token = sessionManager.fetchToken()
        return@combine when {
            isOnline && token == null -> AuthState.NOT_AUTHED
            !isOnline && token == null -> AuthState.NETWORK_ERROR
            isOnline && !token.isNullOrEmpty() -> {
                 if (isTokenActive) {
                    AuthState.SUCCESS
                } else {
                    AuthState.NOT_AUTHED
                }
            }
            !isOnline && !token.isNullOrEmpty() -> AuthState.SUCCESS
            else -> AuthState.CHECKING_TOKEN
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthState.CHECKING_TOKEN
        )
}