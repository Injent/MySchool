package me.injent.myschool.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import me.injent.myschool.core.designsystem.util.rememberMotionScene

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbar(
    scrollBehavior: TopAppBarScrollBehavior,
    pinnedHeight: Dp,
    maxHeight: Dp,
    modifier: Modifier = Modifier
) {
    val pinnedHeightPx: Float
    val maxHeightPx: Float

    LocalDensity.current.run {
        pinnedHeightPx = pinnedHeight.toPx()
        maxHeightPx = maxHeight.toPx()
    }

    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != pinnedHeightPx - maxHeightPx) {
            scrollBehavior.state.heightOffsetLimit = pinnedHeightPx - maxHeightPx
        }
    }

    val progress = LinearEasing.transform(scrollBehavior.state.collapsedFraction)

    val appBarDragModifier = if (!scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
            },
        )
    } else Modifier

    val motionScene = rememberMotionScene(R.raw.collapsing_image_with_search_scene)
    MotionLayout(
        motionScene = motionScene,
        progress = progress,
        modifier = modifier
            .then(appBarDragModifier)
            .systemBarsPadding()
            .fillMaxWidth()
            .height(with(LocalDensity.current) {
                (progress.getValueFromRange(maxHeightPx, pinnedHeightPx)).toDp()
            })
    ) {
        Image(
            modifier = Modifier
                .layoutId("background")
                .fillMaxSize(),
            painter = painterResource(R.drawable.bg_students),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Box(
            modifier = Modifier
                .layoutId("search")
                .fillMaxWidth(0.5f)
                .height(40.dp)
                .background(Color.Black.copy(.15f), CircleShape)
        ) {
            Text(
                text = "Поиск",
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

fun Float.getValueFromRange(start: Float, end: Float): Float {
    return (start + (end - start) * this)
}