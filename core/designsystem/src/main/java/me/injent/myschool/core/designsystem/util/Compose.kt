package me.injent.myschool.core.designsystem.util

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.MotionScene

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