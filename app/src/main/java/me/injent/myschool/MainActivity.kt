package me.injent.myschool

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.ui.MsApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var authState by mutableStateOf(AuthState.CHECKING_TOKEN)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState
                    .onEach {
                        authState = it
                    }
                    .collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            authState == AuthState.CHECKING_TOKEN || authState == AuthState.NETWORK_ERROR
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            if (authState != AuthState.CHECKING_TOKEN && authState != AuthState.NETWORK_ERROR) {
                MySchoolTheme {
                    MsApp(
                        authState = authState,
                    )
                }
            }
            Box(Modifier.fillMaxSize())
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    TODO("Handle permission deny")
                }
            }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}