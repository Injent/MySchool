package me.injent.myschool.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified

@Composable
fun AutoResizableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified
) {
    var resizedTextStyle by remember { mutableStateOf(style) }
    val defaultFontSize = MaterialTheme.typography.bodySmall.fontSize
    var shouldDraw by remember { mutableStateOf(false) }
    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        color = color,
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95f
                )
            } else {
                shouldDraw = true
            }
        }
    )
}

@Composable
fun GradientOverflowText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    collapsedLines: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    gradientColor: Color = Color.Unspecified,
    enabled: Boolean = true
) {
    var isOverflowing by remember { mutableStateOf(false) }
    var shouldDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        modifier = modifier
            .drawWithContent {
                if (shouldDraw)
                    drawContent()

                if (isOverflowing && enabled) {
                    drawRect(
                        brush = Brush.verticalGradient(listOf(
                            Color.Transparent,
                            gradientColor
                        )),
                    )
                }
            },
        color = color,
        style = style,
        maxLines = if (isOverflowing && enabled) {
            collapsedLines
        } else {
            maxLines
        },
        onTextLayout = { result ->
            if (result.lineCount > collapsedLines) {
                isOverflowing = true
            }
            shouldDraw = true
        }
    )
}