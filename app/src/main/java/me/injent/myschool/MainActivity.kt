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
import kotlinx.coroutines.launch
import me.injent.myschool.auth.AccountHelper
import me.injent.myschool.auth.AuthState
import me.injent.myschool.core.data.version.Update
import me.injent.myschool.core.data.version.VersionController
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.ui.MsApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var accountHelper: AccountHelper
    @Inject
    lateinit var versionController: VersionController

    var update: Update? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var authState: AuthState by mutableStateOf(AuthState.CheckingAccounts)

        lifecycleScope.launch {
            authState = accountHelper.checkAuth()
            update = versionController.getUpdate()
        }

        splashScreen.setKeepOnScreenCondition {
            authState == AuthState.CheckingAccounts
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (authState !is AuthState.CheckingAccounts) {
                MySchoolTheme {
                    MsApp(
                        authState = authState,
                        update = update
                    )
                }
            }
            Box(Modifier.fillMaxSize())
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allIsGranted = permissions.all { it.value }
                if (!allIsGranted) {
                    TODO("Handle permission deny")
                }
            }

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