package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.ui.Tag

@Composable
fun UserFeed.Schedule(
    feedUiState: FeedUiState
) {
    when (feedUiState) {
        FeedUiState.Loading -> Unit
        is FeedUiState.Success -> {
            Schedule(
                schedule = feedUiState.todaySchedule
            )
        }
        FeedUiState.Error -> Unit
    }
}

@Composable
private fun Schedule(
    schedule: List<UserFeed.Schedule>
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}