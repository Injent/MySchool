package me.injent.myschool.feature.auth

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.auth.BuildConfig.AUTH_URL
import me.injent.myschool.core.common.sync.SyncState
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
) : ViewModel(), LoginContract {

    private val _state = MutableStateFlow(LoginContract.State())
    override val state: StateFlow<LoginContract.State>
        get() = _state.asStateFlow()

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

    override fun onEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.OnLogin -> {
                authWithWebViewClient(
                    context = event.context,
                    login = state.value.login,
                    password = state.value.password
                )
            }
            is LoginContract.Event.OnLoginChange -> _state.update {
                it.copy(login = event.value)
            }
            is LoginContract.Event.OnPasswordChange -> _state.update {
                it.copy(password = event.value)
            }
        }
    }

    private suspend fun createAccount(accessToken: String) {
        val userContext = loginRepository.login(accessToken)

        userContextRepository.setUserContext(userContext)
        accountHelper.updateAccount(accessToken, userContext)
    }

    private suspend fun startSync(accessToken: String) {
        _state.update { it.copy(status = AuthStatus.Loading) }
        createAccount(accessToken)
        workHelper.startOneTimeSyncWork()
    }

    private fun successLogin() {
        _state.update { it.copy(status = AuthStatus.Success) }
        workHelper.pruneWork()
        workHelper.startPeriodicMarkUpdateWork()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun authWithWebViewClient(
        context: Context, login: String, password: String
    ) {
        _state.update { it.copy(status = AuthStatus.Connecting) }

        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            loadUrl("$AUTH_URL.com")
        }

        webView.webViewClient = AuthWebClient(
            onTokenAcquired = { token ->
                viewModelScope.launch { this@LoginViewModel.startSync(token) }
            },
            onError = { message ->
                _state.update { it.copy(status = AuthStatus.Error(message)) }
            },
            login = login,
            password = password
        )
    }
}