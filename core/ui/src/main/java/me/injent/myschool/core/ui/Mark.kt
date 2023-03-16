package me.injent.myschool.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.injent.myschool.core.designsystem.theme.*

@Composable
fun Mark(
    value: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    alpha: Float = .75f
) {
    val backgroundColor by animateColorAsState(targetValue = when (value.toFloatOrNull() ?: -1f) {
        in 4f..5f -> MaterialTheme.colorScheme.positive
        in 3f..3.99f -> MaterialTheme.colorScheme.warning
        in 0f..2.99f -> MaterialTheme.colorScheme.negative
        else -> MaterialTheme.colorScheme.link
    })
    Text(
        text = value,
        color = color ?: backgroundColor,
        fontSize = 16.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(color = backgroundColor.copy(alpha), shape = MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
fun MarkPreview() {
    MySchoolTheme {
        Mark(value = "5")
    }
}