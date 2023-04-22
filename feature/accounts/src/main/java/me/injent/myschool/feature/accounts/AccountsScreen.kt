package me.injent.myschool.feature.accounts

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.auth.AuthState
import me.injent.myschool.core.auth.BuildConfig
import me.injent.myschool.core.designsystem.component.MsCityBackground
import me.injent.myschool.feature.accounts.model.ExpandedAccount

@Composable
internal fun AccountsRoute(
    onLogin: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val accountsUiState by viewModel.accountsUiState.collectAsStateWithLifecycle()

    AccountsScreen(
        accountsUiState = accountsUiState,
        onSelectAccount = viewModel::selectAccount
    )

    var isLoginLoading by remember { mutableStateOf(false) }

    LaunchedEffect(accountsUiState.authState) {
        when (accountsUiState.authState) {
            is AuthState.Success -> {
                isLoginLoading = false
                onLogin()
            }
            is AuthState.Loading -> isLoginLoading = true
            else -> Unit
        }
    }

    if (isLoginLoading) {
        LoadingOverlay()
    }
}

@Composable
private fun AccountsScreen(
    accountsUiState: AccountsUiState,
    onSelectAccount: (ExpandedAccount) -> Unit
) {
    MsCityBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            val context = LocalContext.current
            AccountCards(
                accountsUiState = accountsUiState,
                onSelectAccount = onSelectAccount,
                onAddAccount = { authInBrowser(context) },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun LoadingOverlay() {
    val interationSource = remember { MutableInteractionSource() }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(.20f))
            .clickable(onClick = {}, indication = null, interactionSource = interationSource)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

private fun authInBrowser(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, BuildConfig.AUTH_URL.toUri())
    context.startActivity(intent)
}