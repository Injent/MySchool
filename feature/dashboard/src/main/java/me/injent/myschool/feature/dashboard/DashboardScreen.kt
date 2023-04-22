package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.component.MsBackgroundWithImageOnTop
import me.injent.myschool.core.designsystem.component.StatusBar
import me.injent.myschool.core.designsystem.util.ignoreHorizontalParentPadding
import me.injent.myschool.core.ui.ScheduleUiState
import me.injent.myschool.core.ui.schedule
import me.injent.myschool.feature.homeworkdialog.LessonDialog


@Composable
internal fun DashboardRoute(
    onMarkClick: (markId: Long) -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scheduleUiState by viewModel.scheduleUiState.collectAsStateWithLifecycle()
    val feedUiState by viewModel.feedUiState.collectAsStateWithLifecycle()

    uiState.viewingHomework?.let { lesson ->
        LessonDialog(
            onDismiss = { viewModel.onEvent(PointEvent.CloseLessonDialog) },
            lesson = lesson
        )
    }

    val currentOnMarkClick by rememberUpdatedState(onMarkClick)

    LaunchedEffect(uiState) {
        with(uiState) {
            when {
                viewingMarkDetails != null -> {
                    currentOnMarkClick(viewingMarkDetails)
                    viewModel.onEvent(PointEvent.BackEvent)
                }
            }
        }
    }

    DashboardScreen(
        feedUiState = feedUiState,
        scheduleUiState = scheduleUiState,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onLogout = onLogout
    )
}

@Composable
private fun DashboardScreen(
    feedUiState: FeedUiState,
    scheduleUiState: ScheduleUiState,
    uiState: DashboardUiState,
    onLogout: () -> Unit,
    onEvent: (PointEvent) -> Unit
) {
    MsBackgroundWithImageOnTop(
        painter = painterResource(me.injent.myschool.core.ui.R.drawable.bg_students)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            greeting(
                uiState.myName,
                onLogout = onLogout
            )
            item {
                Spacer(Modifier.height(16.dp))
            }
            currentLesson(feedUiState)
            item {
                Spacer(Modifier.height(8.dp))
            }
            recentMarks(
                feedUiState = feedUiState,
                onMarkClick = { markId ->
                    onEvent(PointEvent.GoToMarkDetails(markId))
                },
                onRetry = {
                    onEvent(PointEvent.RetryRecentMarks)
                },
                modifier = Modifier.ignoreHorizontalParentPadding(16.dp)
            )
            item {
                Spacer(Modifier.height(4.dp))
            }
            schedule(
                scheduleUiState = scheduleUiState,
                scheduleVariant = uiState.selectedSchedule,
                onClick = { lesson -> onEvent(PointEvent.OpenLessonDialog(lesson)) },
                onChangeVariant = { variant ->
                    onEvent(PointEvent.ChangeScheduleVariant(variant))
                }
            )
        }
        StatusBar()
    }
}

