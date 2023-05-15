package me.injent.myschool.feature.auth

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.auth.BuildConfig.AUTH_URL
import me.injent.myschool.core.common.sync.SyncState
import me.injent.myschool.core.ui.util.BaseViewModel
import me.injent.myschool.core.data.repository.LoginRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.util.SyncProgressMonitor
import me.injent.myschool.core.data.util.SyncStatusMonitor
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
) : BaseViewModel<UiState, UiEvent, Action>() {

    private val _uiState = MutableStateFlow(UiState())
    override val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

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
    }

    override fun processAction(action: Action) = when (action) {
        Action.Back -> sendEvent(UiEvent.Back)
        is Action.ChangeLogin -> _uiState.update {
            it.copy(login = action.value)
        }
        is Action.ChangePassword -> _uiState.update {
            it.copy(password = action.value)
        }
        is Action.Login -> {
            authWithWebViewClient(
                context = action.context,
                login = uiState.value.login,
                password = uiState.value.password
            )
        }
    }

    private suspend fun createAccount(accessToken: String) {
        val userContext = loginRepository.login(accessToken)

        userContextRepository.setUserContext(userContext)
        accountHelper.updateAccount(accessToken, userContext)
    }

    private suspend fun startSync(accessToken: String) {
        _uiState.update { it.copy(authStatus = AuthStatus.Loading) }
        createAccount(accessToken)
        workHelper.startOneTimeSyncWork()
    }

    private fun successLogin() {
        _uiState.update { it.copy(authStatus = AuthStatus.Success) }
        workHelper.pruneWork()
        workHelper.startPeriodicMarkUpdateWork()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun authWithWebViewClient(
        context: Context, login: String, password: String
    ) {
        _uiState.update { it.copy(authStatus = AuthStatus.Connecting) }

        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            loadUrl("$AUTH_URL.com")
        }

        webView.webViewClient = AuthWebClient(
            onTokenAcquired = { token ->
                viewModelScope.launch { this@LoginViewModel.startSync(token) }
            },
            onError = { message ->
                _uiState.update { it.copy(authStatus = AuthStatus.Error(message)) }
            },
            login = login,
            password = password
        )
    }
}