package me.injent.myschool

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.core.data.version.Update
import me.injent.myschool.core.data.version.VersionController
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.ui.MsApp
import me.injent.myschool.ui.PermissionDialog
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var accountHelper: AccountHelper
    @Inject
    lateinit var networkMonitor: NetworkMonitor
    @Inject
    lateinit var versionController: VersionController

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (SDK_INT <= 28) {
            val writeStorage = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]
            if (writeStorage == false) {
                showPermissionDialog = true
            }
        }
    }
    var showPermissionDialog by mutableStateOf(false)

    var update: Update? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var authStatus: AuthStatus by mutableStateOf(AuthStatus.CheckingAccounts)
        lifecycleScope.launch {
            networkMonitor.isOnline
                .onEach {
                    authStatus = accountHelper.checkAuth(it)
                    update = versionController.getUpdate()
                }
                .collect()
        }

        splashScreen.setKeepOnScreenCondition {
            authStatus == AuthStatus.CheckingAccounts
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (authStatus !is AuthStatus.CheckingAccounts) {
                MySchoolTheme {
                    MsApp(
                        authStatus = authStatus,
                        update = update,
                        onUpdate = {
                            lifecycleScope.launch {
                                versionController.installUpdate(update!!)
                            }
                        }
                    )
                }
            }
            Box(Modifier.fillMaxSize())

            if (showPermissionDialog) {
                PermissionDialog(onDismiss = { showPermissionDialog = false; requestPermissions() })
            }
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissionRequest = mutableListOf<String>()
        if (SDK_INT <= 28) {
            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (SDK_INT >= 33) {
            permissionRequest.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        requestPermissionLauncher.launch(permissionRequest.toTypedArray())
    }
}