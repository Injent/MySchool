package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.component.MsBackgroundWithImageOnTop
import me.injent.myschool.core.designsystem.component.StatusBar
import me.injent.myschool.core.designsystem.util.ignoreHorizontalParentPadding
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.feature.homeworkdialog.HomeworkDialog


@Composable
internal fun DashboardRoute(
    onMarkClick: (markId: Long) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val birthdaysUiState by viewModel.birthdaysUiState.collectAsStateWithLifecycle()
    val homeworkUiState by viewModel.feedUiState.collectAsStateWithLifecycle()
    val myName by viewModel.myName.collectAsStateWithLifecycle()

    uiState.viewingHomework?.let { homework ->
        HomeworkDialog(
            onDismiss = {
                viewModel.onEvent(PointEvent.CloseHomeworkDialog)
            },
            homework = homework
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

    NotificationPermRequest()

    DashboardScreen(
        feedUiState = homeworkUiState,
        birthdaysUiState = birthdaysUiState,
        onEvent = viewModel::onEvent,
        myName = myName
    )
}

@Composable
private fun DashboardScreen(
    feedUiState: FeedUiState,
    birthdaysUiState: BirthdaysUiState,
    myName: String,
    onEvent: (PointEvent) -> Unit
) {
    MsBackgroundWithImageOnTop(
        painter = painterResource(me.injent.myschool.core.ui.R.drawable.bg_students)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            greeting(myName)
            item {
                Spacer(Modifier.height(16.dp))
            }
            homeworks(
                feedUiState = feedUiState,
                onClick = { homework ->
                    onEvent(PointEvent.OpenHomeworkDialog(homework))
                }
            )
            item {
                Spacer(Modifier.height(8.dp))
            }
            recentMarks(
                feedUiState = feedUiState,
                onMarkClick = { markId ->
                    onEvent(PointEvent.GoToMarkDetails(markId))
                },
                modifier = Modifier.ignoreHorizontalParentPadding(16.dp)
            )
            item {
                Spacer(Modifier.height(16.dp))
            }
            birthdays(
                birthdaysUiState = birthdaysUiState
            )
        }
        StatusBar()
    }
}

