package me.injent.myschool.feature.markpage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.model.MarkDetails.MarkInfo
import me.injent.myschool.feature.markdetails.R

@Composable
internal fun MarkCard(markDetailsUiState: MarkDetailsUiState) {
    when (markDetailsUiState) {
        MarkDetailsUiState.Loading -> Unit
        is MarkDetailsUiState.Success -> {
            MarkCard(state = markDetailsUiState)
        }
        MarkDetailsUiState.Error -> Unit
    }
}

@Composable
private fun MarkCard(state: MarkDetailsUiState.Success) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MarkContainer(marksInfo = state.markDetails.markInfo)
        }
    }
}

@Composable
private fun MarkContainer(marksInfo: MarkInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.your_mark),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = with(marksInfo) {
                if (marks.size == 1) {
                    marks.first().value
                } else {
                    "${marks[0]}/${marks[1]}"
                }
            },
            fontSize = 32.sp,
            modifier = Modifier
                .border(
                    width = 6.dp,
                    color = MaterialTheme.colorScheme.positive,
                    shape = CircleShape
                )
        )
        Text(
            text = marksInfo.elapsedSetMarkTime,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}