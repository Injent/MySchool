package me.injent.myschool

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.updates.installer.startUpdateService
import me.injent.myschool.updates.monitor.UpdateDownloadMonitor
import me.injent.myschool.updates.versioncontrol.Update
import me.injent.myschool.updates.versioncontrol.VersionController
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    accountHelper: AccountHelper,
    private val versionController: VersionController,
    downloadMonitor: UpdateDownloadMonitor
) : ViewModel() {

    var update: Update? by mutableStateOf(null)
    var authStatus: AuthStatus by mutableStateOf(AuthStatus.CheckingAccounts)

    init {
        viewModelScope.launch {
            networkMonitor.isOnline
                .transformWhile { online ->
                    emit(online)
                    !online
                }
                .onEach {
                    authStatus = accountHelper.checkAuth(it)
                    update = versionController.getUpdate()
                }
                .collect()
        }
    }

    fun onUpdateRequest(context: Context) {
        update?.url?.let { context.startUpdateService(it) }
        update = null
    }
}