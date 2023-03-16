package me.injent.myschool.feature.leaderboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.theme.hint
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.model.PersonAndMarkValue

@Composable
internal fun LeaderboardRoute(
    onBack: () -> Unit,
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val leaderboardUiState by viewModel.leaderboardUiState.collectAsStateWithLifecycle()
    LeaderboardScreen(
        leaderboardUiState = leaderboardUiState,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaderboardScreen(
    leaderboardUiState: LeaderboardUiState,
    onBack: () -> Unit
) {
    val leaderboardListState = rememberLeaderboardListState()
    Scaffold(
        topBar = {
            LeaderboardTopAppBar(
                leaderboardUiState = leaderboardUiState,
                onBack = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            topPersons(
                leaderboardUiState = leaderboardUiState,
                state = leaderboardListState
            )
        }
    }
}

private fun LazyListScope.topPersons(
    leaderboardUiState: LeaderboardUiState,
    state: LeaderboardListState
) {
    when (leaderboardUiState) {
        LeaderboardUiState.Loading -> Unit
        LeaderboardUiState.Error -> Unit
        is LeaderboardUiState.Success -> {
            itemsIndexed(
                items = leaderboardUiState.personsAndMarks,
                key = { _, item -> item.personId }
            ) { index, personAndMarkValue ->
                PersonInLeaderboardItem(
                    place = index + 1,
                    personAndMarkValue = personAndMarkValue,
                    successPercentage = leaderboardUiState.counter.successPercentageInClass(personAndMarkValue.value),
                    isAnimPlayed = state.isAnimationPlayed(index)
                )
                LaunchedEffect(state) {
                    state.playAnimation(index)
                }
            }
        }
    }
}

@Composable
private fun PersonInLeaderboardItem(
    place: Int,
    personAndMarkValue: PersonAndMarkValue,
    successPercentage: Int,
    isAnimPlayed: Boolean
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (isAnimPlayed) {
            progress.snapTo(successPercentage / 100f)
            return@LaunchedEffect
        }
        progress.animateTo(
            targetValue = successPercentage / 100f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .padding(16.dp)
    ) {
        Text(
            text = "${stringResource(R.string.average_mark)} ${personAndMarkValue.value}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$place. ${personAndMarkValue.personName}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Text(
                text = "$successPercentage%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        LinearProgressIndicator(
            progress = progress.value,
            color = MaterialTheme.colorScheme.positive,
            trackColor = MaterialTheme.colorScheme.hint,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaderboardTopAppBar(
    leaderboardUiState: LeaderboardUiState,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            when (leaderboardUiState) {
                LeaderboardUiState.Loading -> Unit
                LeaderboardUiState.Error -> Unit
                is LeaderboardUiState.Success -> {
                    AutoResizableText(
                        text = "${leaderboardUiState.subjectName} " +
                                "• ${stringResource(id = R.string.progress)}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = MsIcons.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun rememberLeaderboardListState(): LeaderboardListState {
    return remember { LeaderboardListState() }
}

data class LeaderboardListState(
    private val playedAnimations: MutableSet<Int> = mutableSetOf()
) {
    fun isAnimationPlayed(index: Int): Boolean
        = playedAnimations.contains(index)

    fun playAnimation(index: Int) {
        playedAnimations.add(index)
    }
}