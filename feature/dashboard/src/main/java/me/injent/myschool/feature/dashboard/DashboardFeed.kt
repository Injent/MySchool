package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.ui.Tag

fun LazyGridScope.greeting(
    name: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val welcomeTextId = remember {
                when (LocalDateTime.currentLocalDateTime().hour) {
                    in 4..11 -> R.string.welcome_text_morning
                    in 12..17 -> R.string.welcome_text_afternoon
                    in 18..22 -> R.string.welcome_text_evening
                    else -> R.string.welcome_text_night
                }
            }
            AutoResizableText(
                text = "${stringResource(welcomeTextId)}, $name!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W900,
                modifier = modifier.weight(0.7f)
            )
            Box(Modifier.weight(.3f)) {
                IconButton(onClick = onLogout, Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        painter = painterResource(MsIcons.Logout),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

fun LazyGridScope.currentLesson(
    feedUiState: FeedUiState
) {
    when (feedUiState) {
        FeedUiState.Error -> Unit
        FeedUiState.Loading -> Unit
        is FeedUiState.Success -> {
            item {
                feedUiState.currentLesson?.let { lesson ->
                    CurrentLesson(
                        lesson = lesson,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentLesson(
    lesson: UserFeed.Lesson,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape
    ) {
        Row(
            modifier = modifier.padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Tag(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = stringResource(R.string.now)
                )
            }
            Text(
                text = lesson.subjectName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}