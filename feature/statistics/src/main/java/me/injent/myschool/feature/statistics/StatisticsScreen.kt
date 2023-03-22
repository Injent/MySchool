package me.injent.myschool.feature.statistics

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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
internal fun StatisticsRoute(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val statisticsUiState by viewModel.averageMarks.collectAsStateWithLifecycle()

    StatisticsScreen(
        statisticsUiState = statisticsUiState
    )
}

@Composable
private fun StatisticsScreen(
    statisticsUiState: StatisticsUiState
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.average_mark_during_week),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(32.dp))

        GradeGraph(
            marks = when (statisticsUiState) {
                is StatisticsUiState.Success -> statisticsUiState.averageMarksByWeek
                else -> emptyList()
            },
            onBarClick = { barValue ->

            },
            yAxisTextColor = MaterialTheme.colorScheme.onBackground,
            xAxisTextColor = MaterialTheme.colorScheme.secondary,
            barColor = MaterialTheme.colorScheme.primary,
            height = 128.dp,
            lineColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .placeholder(
                    visible = statisticsUiState is StatisticsUiState.Loading,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(Modifier.height(64.dp))
        Text(
            text = "В разработке",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}