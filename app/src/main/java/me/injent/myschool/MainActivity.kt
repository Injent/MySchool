package me.injent.myschool

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.injent.myschool.core.designsystem.theme.MySchoolTheme
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.sync.workers.SyncWorker
import me.injent.myschool.ui.MsApp
import javax.inject.Inject

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
                    }
                    .collect()
            }
        }

        // Keep loading screen while checking token
        splashScreen.setKeepOnScreenCondition {
            authState == AuthState.CHECKING_TOKEN
        }

        setContent {
            if (authState != AuthState.CHECKING_TOKEN) {
                MySchoolTheme {
                    MsApp(authState = authState)
                }
            }
        }
    }
}