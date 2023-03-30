package me.injent.myschool.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.common.util.relativeTimeFormat
import me.injent.myschool.core.designsystem.component.AutoResizableText
import me.injent.myschool.core.designsystem.theme.link
import me.injent.myschool.core.designsystem.theme.negative
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.UserFeed

fun LazyGridScope.recentMarks(
    feedUiState: FeedUiState,
    onMarkClick: (markId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (feedUiState) {
        is FeedUiState.Success -> {
            item {
                RecentMarks(
                    marksCards = feedUiState.marksCards,
                    onMarkClick = onMarkClick,
                    modifier = modifier
                )
            }
        }
        FeedUiState.Error -> Unit
        FeedUiState.Loading -> Unit
    }
}

@Composable
private fun RecentMarks(
    marksCards: List<UserFeed.MarkCard>,
    onMarkClick: (markId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(marksCards) { markCard ->
            MarksCard(
                markCard = markCard,
                onClick = { onMarkClick(markCard.marks.first().id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarksCard(
    markCard: UserFeed.MarkCard,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .width(110.dp)
            .height(140.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val markText = remember {
                if (markCard.marks.size == 1) {
                    markCard.marks.first().value
                } else {
                    markCard.marks.map(Mark::value).joinToString("/")
                }
            }
            val markDate = remember {
                markCard.marks.first().date.relativeTimeFormat(LocalDateTime.currentLocalDateTime())
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
                color = markCard.marks.first().mood.toColor(),
                style = TextStyle(fontSize = 26.sp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Text(
                text = markCard.subjectName,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .widthIn(max = 90.dp)
            )
            Text(
                text = markCard.workTypeName,
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
private fun LoadingRecentMarks() {

}

@Composable
private fun Mark.Mood.toColor() =
    when (this) {
        Mark.Mood.Bad -> MaterialTheme.colorScheme.negative
        Mark.Mood.Average -> MaterialTheme.colorScheme.warning
        Mark.Mood.Good -> MaterialTheme.colorScheme.positive
        Mark.Mood.NotSet -> MaterialTheme.colorScheme.link
    }