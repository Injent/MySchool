package me.injent.myschool.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import me.injent.myschool.core.designsystem.component.GradientOverflowText
import me.injent.myschool.core.designsystem.component.MsTextButton
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.core.model.Schedule

sealed interface ScheduleUiState {
    object Loading : ScheduleUiState
    data class Success(
        val todaySchedule: Schedule?,
        val tomorrowSchedule: Schedule?
    ) : ScheduleUiState
    object Error : ScheduleUiState
}

fun LazyGridScope.schedule(
    scheduleUiState: ScheduleUiState,
    scheduleVariant: Schedule.Variant?,
    onChangeVariant: (Schedule.Variant) -> Unit,
    onClick: (Schedule.Lesson) -> Unit
) {
    when (scheduleUiState) {
        ScheduleUiState.Loading -> Unit
        ScheduleUiState.Error -> Unit
        is ScheduleUiState.Success -> {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (variant in Schedule.Variant.values()) {
                        ScheduleButton(
                            selected = variant == scheduleVariant,
                            onClick = { onChangeVariant(variant) },
                            text = if (variant == Schedule.Variant.Today) {
                                stringResource(R.string.today)
                            } else {
                                stringResource(R.string.tomorrow)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            item {
                Spacer(Modifier.height(4.dp))
            }
            item {
                if (scheduleVariant != null) {
                    Schedule(
                        schedule = if (scheduleVariant == Schedule.Variant.Today) {
                            scheduleUiState.todaySchedule
                        } else {
                            scheduleUiState.tomorrowSchedule
                        },
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.surface.copy(.5f)
        }
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    MsTextButton(
        text = text,
        onClick = onClick,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
    )
}

@Composable
private fun Schedule(
    schedule: Schedule?,
    onClick: (Schedule.Lesson) -> Unit,
) {
    Surface(shape = MaterialTheme.shapes.medium) {
        Column {
            if (schedule != null) {
                for (lesson in schedule.lessons) {
                    ScheduleItem(
                        lesson = lesson,
                        onClick = {
                            if (!lesson.homeworkText.isNullOrEmpty())
                                onClick(lesson)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            } else {
                EmptySchedule()
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    lesson: Schedule.Lesson,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            val (number, hours, subject, theme, divider, homework, attachment) = createRefs()

            Text(
                text = "${lesson.number} ${stringResource(R.string.lesson)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.constrainAs(number) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )

            Text(
                text = lesson.hours,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.constrainAs(hours) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
            )

            Text(
                text = lesson.subjectName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.constrainAs(subject) {
                    start.linkTo(number.start)
                    top.linkTo(number.bottom)
                }
            )

            Text(
                text = lesson.theme.ifEmpty { stringResource(R.string.without_theme) },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(theme) {
                    start.linkTo(subject.start)
                    top.linkTo(subject.bottom)
                }
            )

            if (!lesson.homeworkText.isNullOrEmpty()) {
                if (lesson.hasAttachment) {
                    Icon(
                        painter = painterResource(MsIcons.AttachFile),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(20.dp)
                            .constrainAs(attachment) {
                                top.linkTo(divider.bottom, 16.dp)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            }
                    )
                }

                Divider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.constrainAs(divider) {
                        top.linkTo(theme.bottom, 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                GradientOverflowText(
                    text = lesson.homeworkText ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.75f),
                    maxLines = 2,
                    modifier = Modifier.constrainAs(homework) {
                        start.linkTo(parent.start)
                        top.linkTo(divider.top, 12.dp)
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptySchedule() {
    Box(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.no_lessons),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}