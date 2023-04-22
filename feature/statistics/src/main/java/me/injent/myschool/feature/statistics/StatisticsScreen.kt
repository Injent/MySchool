package me.injent.myschool.feature.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.util.ignoreHorizontalParentPadding
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.PeriodType
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.ui.FollowUpdates

@Composable
internal fun StatisticsRoute(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val bestStudents by viewModel.bestStudentsBySubject.collectAsStateWithLifecycle()
    val statisticsUiState by viewModel.statisticsUiState.collectAsStateWithLifecycle()

    StatisticsScreen(
        statisticsUiState = statisticsUiState,
        bestStudents = bestStudents,
        onChangePeriod = viewModel::setPeriod
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    statisticsUiState: StatisticsUiState,
    bestStudents: Map<Subject, Person>,
    onChangePeriod: (Period) -> Unit
) {
    Scaffold(
        topBar = {
            StatisticsTopAppBar(
                statisticsUiState = statisticsUiState,
                onChangePeriod = onChangePeriod
            )
        }
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
        ) {
            item {
                Spacer(Modifier.height(32.dp))
            }
            bestStudents(
                bestStudents = bestStudents,
                modifier = Modifier.ignoreHorizontalParentPadding(16.dp)
            )
            item {
                Spacer(Modifier.height(96.dp))
            }
            item {
                FollowUpdates()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsTopAppBar(
    statisticsUiState: StatisticsUiState,
    onChangePeriod: (Period) -> Unit,
) {
    TopAppBar(
        title = {
            val context = LocalContext.current
            val periodName = remember(statisticsUiState) { with(context) {
                 val name = when (statisticsUiState.selectedPeriod?.type) {
                    PeriodType.HalfYear -> getString(R.string.half_year)
                    PeriodType.Quarter -> getString(R.string.quarter)
                    PeriodType.Semester -> getString(R.string.semester)
                    PeriodType.Trimester -> getString(R.string.trimester)
                    PeriodType.Module -> getString(R.string.module)
                    null -> null
                }
                if (name != null) {
                    "${statisticsUiState.selectedPeriod!!.number + 1} $name"
                } else ""
            }}
            Text(
                text = periodName,
                style = MaterialTheme.typography.titleSmall
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

//GradeGraph(
//            marks = when (statisticsUiState) {
//                is StatisticsUiState.Success -> statisticsUiState.averageMarksByWeek
//                else -> emptyList()
//            },
//            onBarClick = { barValue ->
//
//            },
//            yAxisTextColor = MaterialTheme.colorScheme.onBackground,
//            xAxisTextColor = MaterialTheme.colorScheme.secondary,
//            barColor = MaterialTheme.colorScheme.primary,
//            height = 128.dp,
//            lineColor = MaterialTheme.colorScheme.secondary,
//            modifier = Modifier
//                .fillMaxWidth()
//                .placeholder(
//                    visible = statisticsUiState is StatisticsUiState.Loading,
//                    highlight = PlaceholderHighlight.shimmer()
//                )
//        )