package me.injent.myschool.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@get:Composable
val Shapes.topMedium: Shape
    get() = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)