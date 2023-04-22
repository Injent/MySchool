package me.injent.myschool.feature.markpage

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.injent.myschool.core.designsystem.theme.link
import me.injent.myschool.core.designsystem.theme.negative
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.designsystem.theme.warning
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.MarkDetails
import me.injent.myschool.core.model.MarkDetails.MarkInfo
import me.injent.myschool.feature.markdetails.R

@Composable
internal fun MarkCard(markDetailsUiState: MarkDetailsUiState) {
    when (markDetailsUiState) {
        MarkDetailsUiState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        is MarkDetailsUiState.Success -> {
            if (markDetailsUiState.markDetails.markInfo.marks.size == 1) {
                SingleMarkCard(state = markDetailsUiState)
            } else {
                DoubleMarkCard(state = markDetailsUiState)
            }
        }
        MarkDetailsUiState.Error -> {
            Text(text = stringResource(R.string.failed_to_load_data))
        }
    }
}

@Composable
private fun SingleMarkCard(state: MarkDetailsUiState.Success) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            MarkView(
                marksInfo = state.markDetails.markInfo,
                modifier = Modifier.weight(1f)
            )
            CategoriesView(
                categories = state.markDetails.categories,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DoubleMarkCard(state: MarkDetailsUiState.Success) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            MarkView(
                marksInfo = state.markDetails.markInfo
            )
            CategoriesView(
                categories = state.markDetails.categories
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoriesView(
    categories: List<MarkDetails.Category>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.classmates_marks),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            maxItemsInEachRow = 2
        ) {
            categories.forEach { category ->
                CategoryView(
                    category = category,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CategoryView(
    category: MarkDetails.Category,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .background(
                    color = category.mood.toColor(),
                    shape = CircleShape
                )
        ) {
            Text(
                text = category.value,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Text(
            text = buildAnnotatedString {
                append("${category.studentCount} ${stringResource(R.string.classmates)} ")
//                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
//                    append("(${category.percent}%)")
//                }
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun MarkView(
    marksInfo: MarkInfo,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.your_mark),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            marksInfo.marks.forEach { mark ->
                MarkInCircle(
                    markValue = mark.value,
                    markColor = mark.mood.toColor()
                )
            }
        }

        Text(
            text = marksInfo.elapsedSetMarkTime,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun MarkInCircle(
    markValue: String,
    markColor: Color,
    modifier: Modifier = Modifier,
    borderStrokeWidth: Dp = 6.dp,
    textSize: TextUnit = 32.sp
) {
    val density = LocalDensity.current
    val textHeight = remember {
        with(density) { textSize.toPx() }
    }
    val textPaint = remember {
        Paint().apply {
            textAlign = Paint.Align.CENTER
            this.textSize = textHeight
            color = markColor.toArgb()
            isFakeBoldText = true
        }
    }


    Canvas(modifier = modifier.size(80.dp)) {
        val radius = size.width / 2
        drawCircle(
            brush = SolidColor(markColor),
            style = Stroke(width = borderStrokeWidth.toPx()),
            radius = radius
        )
        drawContext.canvas.nativeCanvas.drawText(
            markValue,
            radius,
            radius + (textHeight / 2),
            textPaint
        )
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