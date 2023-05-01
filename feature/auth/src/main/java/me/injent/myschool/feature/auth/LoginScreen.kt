package me.injent.myschool.feature.auth

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.*
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.designsystem.component.MsCityBackground
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.designsystem.util.use
import me.injent.myschool.feature.auth.LoginContract.Event
import me.injent.myschool.feature.auth.LoginContract.State

private const val TRANSITION_DELAY = 1000L

@Composable
internal fun LoginRoute(
    onLogin: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val (state, event) = use(viewModel)
    val syncProgress by viewModel.syncProgress.collectAsStateWithLifecycle()

    LaunchedEffect(state.status) {
        if (state.status is AuthStatus.Success) {
            delay(TRANSITION_DELAY)
            onLogin()
        }
    }

    LoginScreen(
        state = state,
        syncProgress = syncProgress,
        event = event::invoke
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun LoginScreen(
    state: State,
    event: (Event) -> Unit,
    syncProgress: Int,
) {
    MsCityBackground(modifier = Modifier.navigationBarsPadding()) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoginCard(
                state = state,
                event = event::invoke,
                syncProgress = syncProgress,
                modifier = Modifier
                    .fillMaxWidth(.75f)
                    .align(Alignment.Center)
            )

        }
    }
}

@Composable
private fun LoginCard(
    state: State,
    event: (Event) -> Unit,
    syncProgress: Int,
    modifier: Modifier = Modifier
) {
    var cardHeightPx by remember { mutableStateOf(0) }

    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .defaultMinSize(minHeight = with(LocalDensity.current) { cardHeightPx.toDp() })
            .onSizeChanged {
                if (cardHeightPx < it.height) {
                    cardHeightPx = it.height
                }
            }
    ) {
        when (state.status) {
            AuthStatus.Loading -> LoadingContent(progress = syncProgress)
            AuthStatus.NotAuthed, is AuthStatus.Error, AuthStatus.Connecting -> {
                LoginContent(state = state, event = event::invoke)
            }
            AuthStatus.Success -> Box {
                Text(
                    text = stringResource(id = R.string.successful_login),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> Unit
        }
    }
}

@Composable
private fun LoadingContent(
    @IntRange(from = 0, to = 100) progress: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(MsIcons.Database),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            val animatedProgress by animateFloatAsState(targetValue = progress / 100f)
            LinearProgressIndicator(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.background,
                progress = animatedProgress
            )
            Icon(
                painter = painterResource(MsIcons.AndroidPhone),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = stringResource(id = R.string.receiving_data) + " $progress%",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginContent(
    state: State,
    event: (Event) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        if (state.status is AuthStatus.Error) {
            Text(
                text = state.status.message ?: "",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.warning,
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .fillMaxWidth()
                    .padding(2.dp)
            )
        }
        OutlinedTextField(
            value = state.login,
            onValueChange = { value ->
                if (state.status !is AuthStatus.Connecting)
                    event(Event.OnLoginChange(value))
            },
            singleLine = true,
            placeholder = {
                Text(
                    text = stringResource(R.string.login_field),
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        var passwordVisible by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = state.password,
            onValueChange = { value ->
                if (state.status !is AuthStatus.Connecting)
                    event(Event.OnPasswordChange(value))
            },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            placeholder = {
                Text(
                    text = stringResource(R.string.password_field),
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) MsIcons.Visibility else MsIcons.VisibilityOff
                        ),
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "",
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline
            )
            val context = LocalContext.current
            Button(
                shape = MaterialTheme.shapes.extraSmall,
                onClick = { if (state.status !is AuthStatus.Connecting) event(Event.OnLogin(context)) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .height(ButtonDefaults.MinHeight)
                    .width(120.dp)
            ) {
                if (state.status is AuthStatus.Connecting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                } else {
                    Text(text = stringResource(R.string.login))
                }
            }
        }
    }
}