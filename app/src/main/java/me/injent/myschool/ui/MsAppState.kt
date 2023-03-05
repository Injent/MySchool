package me.injent.myschool.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import me.injent.myschool.feature.authorization.AuthState

@Composable
fun rememberMsAppState(
    authState: AuthState,
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
) : MsAppState {
    return remember(authState, coroutineScope, navController, windowSizeClass) {
        MsAppState(navController, coroutineScope, authState, windowSizeClass)
    }
}

@Stable
class MsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val authState: AuthState,
    val windowSizeClass: WindowSizeClass
)