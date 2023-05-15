package me.injent.myschool.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
inline fun <reified STATE, EVENT, ACTION> use(
    viewModel: BaseViewModel<STATE, EVENT, ACTION>,
): StateDispatchEffect<STATE, EVENT, ACTION> {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event by viewModel.uiEvents.collectAsStateWithLifecycle(null)

    val dispatch: (ACTION) -> Unit = viewModel::applyAction

    return StateDispatchEffect(
        state = state,
        event = event,
        dispatch = dispatch,
    )
}

data class StateDispatchEffect<STATE, EVENT, ACTION>(
    val state: STATE,
    val event: EVENT?,
    val dispatch: (ACTION) -> Unit
)