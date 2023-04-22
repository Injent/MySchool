package me.injent.myschool.feature.personmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.Subject
import me.injent.myschool.core.ui.MarkView

@Composable
internal fun ColumnScope.MarksList(
    personMarksUiState: PersonMarksUiState,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit
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
                    onLeaderboardClick = onLeaderboardClick,
                    onMarkClick = onMarkClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MarksList(
    subjectsToMarks: Map<Subject, List<Mark>>,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for ((subject, marks) in subjectsToMarks) {
            SubjectSection(
                subject = subject,
                marks = marks,
                onLeaderboardClick = onLeaderboardClick,
                onMarkClick = onMarkClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectSection(
    subject: Subject,
    marks: List<Mark>,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        val averageMarkValue by produceState(initialValue = "...") {
            val averageMark = marks.map { it.value.toInt() }.average()
            value = String.format("%.1f", averageMark)
        }
        MarkView(
            value = averageMarkValue,
            color = MaterialTheme.colorScheme.secondary,
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        )
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
                painter = painterResource(MsIcons.Leaderboard),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        marks.forEach { mark ->
            MarkView(
                value = mark.value,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable {
                        onMarkClick(mark.id)
                    }
            )
        }
    }
}