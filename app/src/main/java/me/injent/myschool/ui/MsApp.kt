package me.injent.myschool.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import me.injent.myschool.R
import me.injent.myschool.core.designsystem.component.MsBackground
import me.injent.myschool.core.ui.MsNavigationBarItem
import me.injent.myschool.feature.authorization.AuthState
import me.injent.myschool.feature.authorization.navigation.authorizationRoute
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.navigation.MsNavHost
import me.injent.myschool.navigation.RootDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MsApp(
    authState: AuthState,
    appState: MsAppState = rememberMsAppState(),
) {
    MsBackground {
        val context = LocalContext.current
        LaunchedEffect(authState) {
            if (authState != AuthState.NETWORK_ERROR) return@LaunchedEffect
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG).show()
        }

        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (appState.shouldShowBottomNavigation) {
                    MsBottomNavigation(
                        destinations = appState.rootDestinations,
                        onNavigate = appState::navigateTo,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                MsNavHost(
                    navController = appState.navController,
                    startDestination = if (authState == AuthState.NOT_AUTHED) {
                        authorizationRoute
                    } else {
                        dashboardRoute
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }
}

@Composable
private fun MsBottomNavigation(
    destinations: List<RootDestination>,
    onNavigate: (RootDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (destination in destinations) {
            val selected = currentDestination.isChildOfRootDestination(destination)

            MsNavigationBarItem(
                selected = selected,
                onClick = { onNavigate(destination) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary
            ) {
                val icon = if (selected) {
                    destination.selectedIcon
                } else {
                    destination.unselectedIcon
                }
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(destination.titleTextId),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

fun NavDestination?.isChildOfRootDestination(destination: RootDestination?): Boolean {
    return this?.hierarchy?.any {
        it.route?.contains(destination?.route ?: return@any false, true) ?: false
    } ?: false
}