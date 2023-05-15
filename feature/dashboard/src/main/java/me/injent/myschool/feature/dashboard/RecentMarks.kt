package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.*
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.theme.link
import me.injent.myschool.core.designsystem.theme.negative
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.ui.ErrorCard

fun LazyGridScope.recentMarks(
    feedUiState: FeedUiState,
    onRetry: () -> Unit,
    onMarkClick: (markId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (feedUiState) {
        is FeedUiState.Success -> item {
            RecentMarks(
                marksCards = feedUiState.recentMarks,
                onMarkClick = onMarkClick,
                modifier = modifier
            )
        }
        FeedUiState.Error -> item {
            ErrorCard(
                onRetry = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(minCardSize)
            )
        }
        FeedUiState.Loading -> item {
            LoadingRecentMarks(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun RecentMarks(
    marksCards: List<UserFeed.RecentMark>,
    onMarkClick: (markId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (marksCards.isEmpty()) {
        Box(
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Text(
                text = stringResource(R.string.you_havent_marks_yet),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(marksCards) { markCard ->
                MarkCard(
                    recentMark = markCard,
                    onClick = { onMarkClick(markCard.marks.first().id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarkCard(
    recentMark: UserFeed.RecentMark,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(110.dp)
            .heightIn(minCardSize),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val markText = remember {
                if (recentMark.marks.size == 1) {
                    recentMark.marks.first().value
                } else {
                    recentMark.marks.map(UserFeed.Mark::value).joinToString("/")
                }
            }
            val markDate = remember {
                recentMark.date.relativeTimeFormat(LocalDateTime.currentLocalDateTime())
            }
            Text(
                text = markDate,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp, end = 8.dp)
            )
            AutoResizableText(
                text = markText,
                color = recentMark.marks.first().mood.toColor(),
                style = TextStyle(fontSize = 26.sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Text(
                text = recentMark.subjectName,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .widthIn(max = 90.dp)
            )
            val formatDate = remember {
                recentMark.lessonDate?.date?.format(MONTH_DATE_FORMAT) ?: recentMark.markTypeText
            }
            Text(
                text = formatDate,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
private fun LoadingRecentMarks(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Spacer(Modifier.width(8.dp))
        repeat(5) {
            Spacer(
                modifier = Modifier
                    .width(110.dp)
                    .height(140.dp)
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        shape = MaterialTheme.shapes.medium
                    )
            )
        }
        Spacer(Modifier.width(8.dp))
    }
}

@Composable
private fun Mark.Mood.toColor() =
    when (this) {
        Mark.Mood.Bad -> MaterialTheme.colorScheme.negative
        Mark.Mood.Average -> MaterialTheme.colorScheme.warning
        Mark.Mood.Good -> MaterialTheme.colorScheme.positive
        Mark.Mood.NotSet -> MaterialTheme.colorScheme.link
    }

private val minCardSize: Dp
    get() = 140.dp