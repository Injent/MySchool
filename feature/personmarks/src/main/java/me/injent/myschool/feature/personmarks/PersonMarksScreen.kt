package me.injent.myschool.feature.personmarks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.topMedium
import me.injent.myschool.core.model.Sex
import me.injent.myschool.core.model.alias.SubjectsToMarks
import me.injent.myschool.core.ui.*

@Composable
internal fun PersonMarksRoute(
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    viewModel: PersonMarksViewModel = hiltViewModel()
) {
    val subjectsAndMarks by viewModel.personMarksUiState.collectAsStateWithLifecycle()
    val personUiState by viewModel.personUiState.collectAsStateWithLifecycle()

    PersonMarksScreen(
        personMarksUiState = subjectsAndMarks,
        personUiState = personUiState,
        onBack = onBack,
        onLeaderboardClick = onLeaderboardClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonMarksScreen(
    personMarksUiState: PersonMarksUiState,
    personUiState: PersonUiState,
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            when (personUiState) {
                is PersonUiState.Success -> {
                    ProfileTopAppBar(
                        onBack = onBack,
                        onProfileClick = {  },
                        personShortName = personUiState.person.shortName
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
                .clip(MaterialTheme.shapes.topMedium)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState()),
        ) {
            when (personUiState) {
                is PersonUiState.Success -> {
                    Text(
                        text = if (personUiState.person.sex == Sex.Male) {
                            stringResource(id = R.string.male_student_marks)
                        } else {
                            stringResource(id = R.string.female_student_marks)       
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> Unit
            }
            MarksListContainer(
                personMarksUiState = personMarksUiState,
                onLeaderboardClick = onLeaderboardClick
            )
        }
    }
}

@Composable
private fun ColumnScope.MarksListContainer(
    personMarksUiState: PersonMarksUiState,
    onLeaderboardClick: (subjectId: Long) -> Unit
) {
    when (personMarksUiState) {
        PersonMarksUiState.Loading -> Unit
        PersonMarksUiState.Error -> Unit
        is PersonMarksUiState.Success -> {
            val isVisible by produceState(initialValue = false) {
                delay(1L)
                value = true
            }
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                MarksList(
                    subjectsToMarks = personMarksUiState.subjectsToMarks,
                    onLeaderboardClick = onLeaderboardClick
                )
            }
        }
    }
}

@Composable
private fun MarksList(
    subjectsToMarks: SubjectsToMarks,
    onLeaderboardClick: (subjectId: Long) -> Unit
) {
    Column {
        for ((subject, marks) in subjectsToMarks) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = subject.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .align(Alignment.Center)
                )
                IconButton(
                    onClick = { onLeaderboardClick(subject.id) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = painterResource(id = MsIcons.Leaderboard),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            for (marksRow in marks.chunked(16)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Spacer(Modifier.width(16.dp))
                    for (mark in marksRow) {
                        Mark(
                            value = mark.value,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}