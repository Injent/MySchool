package me.injent.myschool.core.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.DEFAULT_DATE_TIME_FORMAT
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.common.util.format
import me.injent.myschool.core.designsystem.component.GradientOverflowText
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.designsystem.util.conditional
import me.injent.myschool.core.model.Homework
import me.injent.myschool.core.model.Attachment

fun LazyGridScope.greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
        ) {
            val welcomeTextId = remember {
                when (LocalDateTime.currentLocalDateTime().hour) {
                    in 4..11 -> R.string.welcome_text_morning
                    in 12..17 -> R.string.welcome_text_afternoon
                    in 18..22 -> R.string.welcome_text_evening
                    else -> R.string.welcome_text_night
                }
            }
            Text(
                text = "${stringResource(welcomeTextId)}, $name!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.W900,
                modifier = modifier
                    .align(Alignment.TopStart)
                    .padding(top = 32.dp)
            )
        }
    }
}

sealed interface HomeworkUiState {
    object Loading : HomeworkUiState
    object Empty : HomeworkUiState
    data class Success(
        val homeworks: List<Homework>
    ) : HomeworkUiState
}

fun LazyGridScope.homeworks(
    homeworkUiState: HomeworkUiState,
    onClick: (Homework) -> Unit
) {
    when (homeworkUiState) {
        HomeworkUiState.Loading -> Unit
        HomeworkUiState.Empty -> Unit
        is HomeworkUiState.Success -> {
            item {
                Text(
                    text = stringResource(R.string.homeworks),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    for (homework in homeworkUiState.homeworks) {
                        HomeworkItem(
                            homework = homework,
                            onClick = { onClick(homework) },
                            contentPadding = PaddingValues(16.dp),
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 24.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeworkItem(
    onClick: () -> Unit,
    homework: Homework,
    contentPadding: PaddingValues
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    color = MaterialTheme.colorScheme.primary.copy(.15f)
                )
            )
            .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = homework.subject.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = homework.sentDate.format(DEFAULT_DATE_TIME_FORMAT),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            GradientOverflowText(
                text = homework.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                gradientColor = MaterialTheme.colorScheme.surface,
                collapsedLines = 2,
            )
            if (homework.files.isNotEmpty()) {
                Icon(
                    painter = painterResource(MsIcons.AttachFile),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}