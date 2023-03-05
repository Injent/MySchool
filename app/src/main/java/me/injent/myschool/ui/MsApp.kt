package me.injent.myschool.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import me.injent.myschool.R
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.feature.authorization.navigation.AUTHORIZATION_ROUTE
import me.injent.myschool.feature.profile.navigation.PROFILE_ROUTE
import me.injent.myschool.feature.profile.navigation.navigateToProfile
import me.injent.myschool.navigation.MsNavHost

@Composable
fun MsApp(
    authState: AuthState,
    windowSizeClass: WindowSizeClass,
    appState: MsAppState = rememberMsAppState(
        authState = authState,
        windowSizeClass = windowSizeClass
    ),
) {
    val context = LocalContext.current
    LaunchedEffect(authState) {
        if (authState != AuthState.NETWORK_ERROR) return@LaunchedEffect
        Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG).show()
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MsNavHost(
            navController = appState.navController,
            startDestination = if (authState == AuthState.NOT_AUTHED) {
                AUTHORIZATION_ROUTE
            } else {
                LaunchedEffect(Unit) {
                    delay(100)
                    appState.navController.navigateToProfile(1000000822018)
                }
                AUTHORIZATION_ROUTE
            }
        )
    }
}