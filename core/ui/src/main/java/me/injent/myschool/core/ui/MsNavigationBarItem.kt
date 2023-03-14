package me.injent.myschool.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RowScope.MsNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedContentColor: Color,
    unselectedContentColor: Color,
    content: @Composable () -> Unit
) {
    val contentColor by animateColorAsState(targetValue = if (selected) {
        selectedContentColor
    } else {
        unselectedContentColor
    })

    Column(
        modifier = modifier
            .weight(1f)
            .clickable(
                onClick = onClick,
                interactionSource = MutableInteractionSource(),
                indication = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}