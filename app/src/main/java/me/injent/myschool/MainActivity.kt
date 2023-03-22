package me.injent.myschool

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.sync.startPeriodicMarkUpdateWork
import me.injent.myschool.ui.MsApp

/**
 * [Application] class for SchoolStat
 */
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
                        if (authState == AuthState.SUCCESS) {
                            WorkManager.getInstance(this@MainActivity)
                                .startPeriodicMarkUpdateWork()
                        }
                    }
                    .collect()
            }
        }

        // Keep loading screen while checking token
        splashScreen.setKeepOnScreenCondition {
            authState == AuthState.CHECKING_TOKEN || authState == AuthState.NETWORK_ERROR
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MySchoolTheme {
                MsApp(
                    authState = authState,
                )
            }
        }
    }
}