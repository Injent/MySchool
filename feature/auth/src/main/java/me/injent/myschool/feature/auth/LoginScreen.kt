package me.injent.myschool.feature.auth

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import me.injent.myschool.auth.AuthState
import me.injent.myschool.core.designsystem.component.MsButton
import me.injent.myschool.core.designsystem.component.MsCityBackground
import me.injent.myschool.core.designsystem.theme.hint

private const val TRANSITION_DELAY = 1000L

@Composable
internal fun LoginRoute(
    onLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val syncProgress by viewModel.syncProgress.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            delay(TRANSITION_DELAY)
            onLogin()
        }
    }

    LoginScreen(
        authState = authState,
        syncProgress = syncProgress
    )
}

@Composable
private fun LoginScreen(
    authState: AuthState,
    syncProgress: Int
) {
    MsCityBackground(modifier = Modifier.navigationBarsPadding()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoginView(
                authState = authState,
                progress = syncProgress,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun BoxScope.LoginView(
    authState: AuthState,
    progress: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth(.75f)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.dnevnik_logo),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(ButtonDefaults.MinHeight)) {
            AuthStateBoxContent(
                authState = authState,
                progress = progress
            )
        }
    }
}

@Composable
private fun BoxScope.AuthStateBoxContent(
    authState: AuthState,
    progress: Int = -1
) {
    when (authState) {
        AuthState.Success -> {
            Text(
                text = stringResource(id = R.string.successful_login),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AuthState.Loading -> {
            Text(
                text = stringResource(id = R.string.receiving_data) + " $progress%",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
            val animatedProgress by animateFloatAsState(targetValue = progress / 100f)
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(80.dp)
                    .height(4.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.hint,
                progress = animatedProgress
            )
        }
        else -> {
            val context = LocalContext.current
            MsButton(
                onClick = { authInBrowser(context) },
                modifier = Modifier.height(ButtonDefaults.MinHeight)
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun authInBrowser(context: Context) {
    val intent = Intent(ACTION_VIEW, me.injent.myschool.core.auth.BuildConfig.AUTH_URL.toUri())
    context.startActivity(intent)
}