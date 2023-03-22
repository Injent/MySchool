package me.injent.myschool.feature.statistics

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import me.injent.myschool.core.common.util.MarkDataCounter
import me.injent.myschool.core.designsystem.theme.topCurvedMedium
import me.injent.myschool.core.designsystem.util.conditional

@Composable
fun GradeGraph(
    marks: List<Float>,
    onBarClick: (value: Float) -> Unit,
    modifier: Modifier = Modifier,
    xAxisTextColor: Color,
    yAxisTextColor: Color,
    barColor: Color,
    lineColor: Color,
    height: Dp,
) {
    val weeks = listOf("П", "В", "С", "Ч", "П", "C", "В")
    val linePaddingEnd = 50f
    val density = LocalDensity.current

    val markCounter = remember {
        MarkDataCounter(2f, 5f)
    }
    var graphIntSize by remember { mutableStateOf(IntSize.Zero) }

    val textPaint = remember {
        Paint().apply {
            textSize = with(density) { 12.sp.toPx() }
            textAlign = Paint.Align.CENTER
            color = yAxisTextColor.toArgb()
        }
    }

    Box(
        modifier = modifier
            .height(height)
            .onSizeChanged {
                graphIntSize = it
            }
    ) {
        var viewingValue: Float? by remember { mutableStateOf(null) }

        Text(
            text = if (viewingValue != null) {
                "${stringResource(R.string.average_mark_during_day)} $viewingValue"
            } else "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(y = (-24).dp)
                .fillMaxWidth()
                .conditional(viewingValue != null) {
                    background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                }
                .padding(vertical = 2.dp)
        )
        Box(
            modifier = Modifier.fillMaxHeight(),
        ) {
            val averageLineOffsetY = remember(graphIntSize) {
                (graphIntSize.height * markCounter.markInFloat(marks.average().toFloat())).toInt()
            }

            Spacer(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .offset {
                        IntOffset(x = 0, y = -averageLineOffsetY)
                    }
                    .drawWithContent {
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)

                        drawLine(
                            color = barColor,
                            start = Offset.Zero,
                            end = Offset(x = size.width - linePaddingEnd, y = 0f),
                            pathEffect = pathEffect,
                            strokeWidth = 3.dp.value,
                            cap = StrokeCap.Round
                        )
                    }
            )
            (2..5).forEach { markMeasure ->
                val lineOffsetY = remember(graphIntSize) {
                    (graphIntSize.height * markCounter.markInFloat(markMeasure.toFloat())).toInt()
                }

                Spacer(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .offset {
                            IntOffset(0, -lineOffsetY)
                        }
                        .drawWithContent {
                            drawLine(
                                color = lineColor,
                                start = Offset(0f, size.height),
                                end = Offset(size.width - linePaddingEnd, size.height)
                            )
                            drawContext.canvas.nativeCanvas.apply {
                                drawText(
                                    markMeasure.toString(),
                                    size.width - 20f,
                                    (textPaint.textSize - 15f) / 2f,
                                    textPaint
                                )
                            }
                        }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .matchParentSize()
                .padding(end = 32.dp, start = 16.dp),
        ) {
            var viewingBarIndex: Int? by remember { mutableStateOf(null) }

            marks.forEachIndexed { index, mark ->
                Box {
                    var animationTriggered by rememberSaveable { mutableStateOf(false) }

                    val graphBarFraction by animateFloatAsState(
                        targetValue = if (animationTriggered) markCounter.markInFloat(mark) else 0f,
                        animationSpec = tween(durationMillis = 1000)
                    )
                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }

                    if (index == viewingBarIndex) {
                        Spacer(
                            modifier = Modifier
                                .offset(y = (-32).dp)
                                .align(Alignment.BottomCenter)
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.secondary)
                        )
                    }

                    val interactionSource = remember { MutableInteractionSource() }
                    GraphBar(
                        fraction = graphBarFraction,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.topCurvedMedium,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    viewingBarIndex = index
                                    viewingValue = mark
                                    onBarClick(mark)
                                }
                            )
                    )
                    Text(
                        text = weeks[index],
                        style = MaterialTheme.typography.labelSmall,
                        color = xAxisTextColor,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 24.dp)
                    )
                }
            }
        }
    }

}

@Composable
private fun GraphBar(
    fraction: Float,
    modifier: Modifier = Modifier,
    color: Color,
    shape: Shape
) {
    require(fraction in 0f..1f) { "Value must be a float from 0f to 1f" }

    Spacer(
        modifier = modifier
            .width(16.dp)
            .fillMaxHeight(fraction)
            .background(color, shape)
    )
}
