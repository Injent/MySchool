package me.injent.myschool.feature.personmarks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.theme.topCurvedMedium
import me.injent.myschool.core.model.Sex
import me.injent.myschool.core.ui.*

@Composable
internal fun PersonMarksRoute(
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit,
    viewModel: PersonMarksViewModel = hiltViewModel()
) {
    val personMarksUiState by viewModel.personMarksUiState.collectAsStateWithLifecycle()
    val personUiState by viewModel.personUiState.collectAsStateWithLifecycle()

    PersonMarksScreen(
        personMarksUiState = personMarksUiState,
        personUiState = personUiState,
        onBack = onBack,
        onLeaderboardClick = onLeaderboardClick,
        onMarkClick = onMarkClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonMarksScreen(
    personMarksUiState: PersonMarksUiState,
    personUiState: PersonUiState,
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit
) {
    Scaffold(
        topBar = {
            when (personUiState) {
                is PersonUiState.Success -> {
                    DefaultTopAppBar(
                        onBack = onBack,
                        title = personUiState.person.shortName,
                        actions = {
                            ProfilePicture(
                                avatarUrl = personUiState.person.avatarUrl,
                                modifier = Modifier.padding(ButtonDefaults.IconSpacing)
                            )
                        },
                    )
                }
                else -> Unit
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize()
                .clip(MaterialTheme.shapes.topCurvedMedium)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState()),
        ) {

            Text(
                text = when (personUiState) {
                    is PersonUiState.Success -> {
                        if (personUiState.person.sex == Sex.Male)
                            stringResource(id = R.string.male_student_marks)
                        else
                            stringResource(id = R.string.female_student_marks)
                    }
                    else -> ""
                },
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )

            MarksList(
                personMarksUiState = personMarksUiState,
                onLeaderboardClick = onLeaderboardClick,
                onMarkClick = { markId ->
                    /*
                      User can view only own mark details
                      This condition active when user viewing his/her's marks
                    */
                    if (personUiState is PersonUiState.Success && personUiState.isMe) {
                        onMarkClick(markId)
                    }
                }
            )
        }
    }
}