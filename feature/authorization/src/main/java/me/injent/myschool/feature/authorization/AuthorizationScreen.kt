package me.injent.myschool.feature.authorization

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import me.injent.myschool.feature.authorization.navigation.AUTH_URL
import me.injent.myschool.core.designsystem.component.MsButton
import me.injent.myschool.core.designsystem.theme.hintText

private const val TRANSITION_FROM_AUTH_SCREEN_DELAY = 1000L

@Composable
internal fun AuthorizationRoute(
    onAuthorization: () -> Unit,
    viewModel: AuthorizationViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    LaunchedEffect(authState) {
        if (authState == AuthState.SUCCESS) {
            delay(TRANSITION_FROM_AUTH_SCREEN_DELAY)
            onAuthorization()
        }
    }
    AuthorizationScreen(
        authState = authState
    )
}

@Composable
internal fun AuthorizationScreen(
    authState: AuthState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_auth),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )
        val cardShape = MaterialTheme.shapes.medium
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(.75f)
                .shadow(
                    elevation = 4.dp,
                    shape = cardShape,
                    clip = false,
                    spotColor = Color.Black.copy(.25f)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape
                )
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.dnevnik_logo),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier.height(ButtonDefaults.MinHeight)) {
                AuthStateBoxContent(authState = authState)
            }
        }
        val context = LocalContext.current
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = MaterialTheme.colorScheme.hintText,
                    textDecoration = TextDecoration.Underline)
                ) {
                    append(stringResource(id = R.string.terms_of_use))
                }
            },
            style = MaterialTheme.typography.bodySmall,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "http://license_url".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-48).dp)
        )
    }
}

@Composable
private fun BoxScope.AuthStateBoxContent(authState: AuthState) {
    when (authState) {
        AuthState.SUCCESS -> {
            Text(
                text = stringResource(id = R.string.successful_login),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AuthState.LOADING -> {
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(80.dp)
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = Color.LightGray,
                trackColor = MaterialTheme.colorScheme.primary
            )
        }
        else -> {
            val context = LocalContext.current
            MsButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, AUTH_URL.toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier.height(ButtonDefaults.MinHeight)
            ) {
                Text(text = stringResource(id = R.string.login), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}