package me.injent.myschool.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.component.MsButton
import me.injent.myschool.core.designsystem.theme.positive

@Composable
internal fun ProfileRoute(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    ProfileScreen(
        profileUiState = profileUiState,
        onLogout = onLogout
    )
}

@Composable
private fun ProfileScreen(
    profileUiState: ProfileUiState,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(MaterialTheme.colorScheme.positive)
        )
        ProfileInfo(
            profileUiState = profileUiState,
            modifier = Modifier.offset(y = (-16).dp)
        )
        Spacer(Modifier.height(64.dp))
        Text(
            text = "В разработке",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        MsButton(onClick = onLogout) {
            Text(text = stringResource(R.string.logout))
        }
    }
}

