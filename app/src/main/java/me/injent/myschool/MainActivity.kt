package me.injent.myschool

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.ui.MsApp
import me.injent.myschool.ui.PermissionDialog
import me.injent.myschool.updates.installer.ACTION_UPDATE_DOWNLOADED
import me.injent.myschool.updates.installer.openUpdateApk


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
    private var showPermissionDialog by mutableStateOf(false)
    private var updateDownloadProgress: Int? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        handleIntent(intent)
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.authStatus == AuthStatus.CheckingAccounts
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (viewModel.authStatus !is AuthStatus.CheckingAccounts) {
                MySchoolTheme {
                    MsApp(
                        authStatus = viewModel.authStatus,
                        update = viewModel.update,
                        onUpdateRequest = { viewModel.onUpdateRequest(this) }
                    )
                }
            }
            // Used for calculation of insets on the screen while MsApp composable is loading
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

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_UPDATE_DOWNLOADED -> {
                if (intent.data != null) this.openUpdateApk(intent.data!!)
            }
        }
    }
}