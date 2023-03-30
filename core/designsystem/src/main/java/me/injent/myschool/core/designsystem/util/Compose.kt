package me.injent.myschool.core.designsystem.util

import androidx.annotation.RawRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.MotionScene
import me.injent.myschool.core.designsystem.theme.negative
import me.injent.myschool.core.designsystem.theme.positive
import me.injent.myschool.core.designsystem.theme.warning

@Composable
fun Modifier.conditional(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier())
    } else this
}

@Composable
fun rememberMotionScene(@RawRes resId: Int): MotionScene {
    val motionScene = MotionScene(
        LocalContext.current.resources.openRawResource(resId).readBytes().decodeToString()
    )
    return remember(resId) { motionScene }
}

fun Modifier.ignoreHorizontalParentPadding(horizontal: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val overridenWidth = constraints.maxWidth + 2 * horizontal.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxWidth = overridenWidth))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}