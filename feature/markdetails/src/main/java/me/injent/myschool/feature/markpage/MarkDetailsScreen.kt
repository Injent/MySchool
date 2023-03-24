package me.injent.myschool.feature.markpage

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.ui.DefaultTopAppBar

@Composable
internal fun MarkDetailsRoute(
    viewModel: MarkDetailsViewModel = hiltViewModel()
) {
    val markDetailsUiState by viewModel.markDetailsUiState.collectAsStateWithLifecycle()

    MarkDetailsScreen(
        onBack = {},
        markDetailsUiState = markDetailsUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarkDetailsScreen(
    onBack: () -> Unit,
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


    }
}