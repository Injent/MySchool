package me.injent.myschool.feature.profile

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.hint
import me.injent.myschool.core.designsystem.theme.topMedium
import me.injent.myschool.core.model.alias.SubjectsAndMarks
import me.injent.myschool.core.ui.*

@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val subjectsAndMarks by viewModel.subjectTableUiState.collectAsStateWithLifecycle()
    val personUiState by viewModel.personUiState.collectAsStateWithLifecycle()
    ProfileScreen(
        onLeaderboardClick = {},
        subjectsAndMarks = subjectsAndMarks,
        personUiState = personUiState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    onLeaderboardClick: () -> Unit,
    subjectsAndMarks: SubjectsAndMarks,
    personUiState: PersonUiState,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            when (personUiState) {
                is PersonUiState.Success -> {
                    ProfileTopAppBar(
                        onBack = {},
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
                .padding(padding)
                .fillMaxSize()
                .clip(MaterialTheme.shapes.topMedium)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = stringResource(id = R.string.student_marks),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
            for ((subject, marks) in subjectsAndMarks) {
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
                        onClick = onLeaderboardClick,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = MsIcons.Leaderboard),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.hint
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
                            Mark(value = mark.value)
                        }
                        Spacer(Modifier.width(16.dp))
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}