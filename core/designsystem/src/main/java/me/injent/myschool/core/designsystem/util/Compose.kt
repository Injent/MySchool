package me.injent.myschool.core.designsystem.util

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.MotionScene
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.injent.myschool.core.common.util.UnidirectionalViewModel

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

@Composable
inline fun <reified STATE, EVENT> use(
    viewModel: UnidirectionalViewModel<STATE, EVENT>,
): StateDispatchEffect<STATE, EVENT> {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val dispatch: (EVENT) -> Unit = viewModel::onEvent

    return StateDispatchEffect(
        state = state,
        dispatch = dispatch,
    )
}

data class StateDispatchEffect<STATE, EVENT>(
    val state: STATE,
    val dispatch: (EVENT) -> Unit
)