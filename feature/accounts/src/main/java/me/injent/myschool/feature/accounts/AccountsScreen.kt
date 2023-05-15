package me.injent.myschool.feature.accounts

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.core.designsystem.component.MsCityBackground
import me.injent.myschool.core.ui.util.use
import me.injent.myschool.feature.accounts.model.UserAccount

@Composable
internal fun AccountsRoute(
    onLogin: () -> Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val (uiState, event, action) = use(viewModel)

    AccountsScreen(
        uiState = uiState,
        onSelectAccount = viewModel::selectAccount
    )

    var isLoginLoading by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.authStatus) {
        when (uiState.authStatus) {
            is AuthStatus.Success -> {
                isLoginLoading = false
                onLogin()
            }
            is AuthStatus.Loading -> isLoginLoading = true
            else -> Unit
        }
    }

    if (isLoginLoading) {
        LoadingOverlay()
    }
}

@Composable
private fun AccountsScreen(
    uiState: UiState,
    onSelectAccount: (UserAccount) -> Unit
) {
    MsCityBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            AccountCards(
                uiState = uiState,
                onSelectAccount = onSelectAccount,
                onAddAccount = {},
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