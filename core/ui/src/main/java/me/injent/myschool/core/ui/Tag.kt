package me.injent.myschool.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    shape: Shape = MaterialTheme.shapes.small,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(containerColor, shape)
            .padding(contentPadding)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall,
            LocalContentColor provides contentColor
        ) {
            content()
        }
    }
}