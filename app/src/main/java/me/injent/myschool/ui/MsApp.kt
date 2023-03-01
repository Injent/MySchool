package me.injent.myschool.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.feature.authorization.navigation.AUTHORIZATION_ROUTE
import me.injent.myschool.navigation.MsNavHost

@Composable
fun MsApp(
    authState: AuthState,
    appState: MsAppState = rememberMsAppState(authState = authState)
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MsNavHost(
            navController = appState.navController,
            startDestination = if (authState == AuthState.NOT_AUTHED) {
                AUTHORIZATION_ROUTE
            } else {
                "A"
            }
        )
    }
}