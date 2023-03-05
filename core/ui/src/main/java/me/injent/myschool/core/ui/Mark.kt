package me.injent.myschool.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.injent.myschool.core.designsystem.theme.*

@Composable
fun Mark(value: String) {
    val backgroundColor = animateColorAsState(targetValue = when (value) {
        "5", "4" -> MaterialTheme.colorScheme.positive
        "3" -> MaterialTheme.colorScheme.warning
        "2" -> MaterialTheme.colorScheme.negative
        else -> MaterialTheme.colorScheme.link
    })
    Text(
        text = value,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color = backgroundColor.value.copy(.75f), shape = MaterialTheme.shapes.extraSmall)
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