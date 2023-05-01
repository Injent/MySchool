package me.injent.myschool.feature.markpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.ui.DefaultTopAppBar

@Composable
internal fun MarkDetailsRoute(
    onBack: () -> Unit,
    viewModel: MarkDetailsViewModel = hiltViewModel()
) {
    val markDetailsUiState by viewModel.markDetailsUiState.collectAsStateWithLifecycle()

    MarkDetailsScreen(
        onBack = onBack,
        markDetailsUiState = markDetailsUiState,
        onRetry = viewModel::reloadData
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarkDetailsScreen(
    onBack: () -> Unit,
    onRetry: () -> Unit,
    markDetailsUiState: MarkDetailsUiState
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                onBack = onBack,
                title = if (markDetailsUiState is MarkDetailsUiState.Success) {
                    markDetailsUiState.subject.name
                } else ""
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            MarkCard(
                markDetailsUiState = markDetailsUiState,
                onRetry = onRetry
            )
        }
    }
}