package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.component.MsBackgroundWithImageOnTop
import me.injent.myschool.core.model.Homework
import me.injent.myschool.feature.homeworkdialog.HomeworkDialog


@Composable
internal fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val birthdaysUiState by viewModel.birthdaysUiState.collectAsStateWithLifecycle()
    val homeworkUiState by viewModel.homeworkUiState.collectAsStateWithLifecycle()
    val myName by viewModel.myName.collectAsStateWithLifecycle()

    var viewingHomework: Homework? by rememberSaveable { mutableStateOf(null) }
    viewingHomework?.let { homework ->
        HomeworkDialog(
            onDismiss = { viewingHomework = null },
            homework = homework
        )
    }

    NotificationPermRequest()

    DashboardScreen(
        homeworkUiState = homeworkUiState,
        birthdaysUiState = birthdaysUiState,
        onClick = { homework ->
            viewingHomework = homework
        },
        myName = myName
    )
}

@Composable
private fun DashboardScreen(
    homeworkUiState: HomeworkUiState,
    birthdaysUiState: BirthdaysUiState,
    myName: String,
    onClick: (Homework) -> Unit
) {
    MsBackgroundWithImageOnTop(painter = painterResource(me.injent.myschool.core.ui.R.drawable.bg_students)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            greeting(myName)
            homeworks(
                homeworkUiState = homeworkUiState,
                onClick = onClick
            )
            birthdays(
                birthdaysUiState = birthdaysUiState
            )
        }
    }
}

