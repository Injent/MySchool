package me.injent.myschool.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun LazyListScope.bottomBarSpacer() {
    item {
        BottomBarSpacer()
    }
}

fun LazyGridScope.bottomBarSpacer() {
   item {
       BottomBarSpacer()
   }
}

@Composable
fun BottomBarSpacer() {
    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
}