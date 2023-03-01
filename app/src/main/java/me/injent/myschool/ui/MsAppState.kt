package me.injent.myschool.ui

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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
) : MsAppState {
    return remember(authState, coroutineScope, navController) {
        MsAppState(navController, coroutineScope, authState)
    }
}

@Stable
class MsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val authState: AuthState
)