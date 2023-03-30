package me.injent.myschool.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun StatusBar(
    color: Color = Color.Black,
    alpha: Float = .15f
) {
    Spacer(
        Modifier
            .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .fillMaxWidth()
            .background(color.copy(alpha))
    )
}