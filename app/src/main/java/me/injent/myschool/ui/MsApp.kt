package me.injent.myschool.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import me.injent.myschool.R
import me.injent.myschool.auth.AuthStatus
import me.injent.myschool.updates.versioncontrol.Update
import me.injent.myschool.core.designsystem.component.MsBackground
import me.injent.myschool.core.ui.MsNavigationBarItem
import me.injent.myschool.feature.auth.navigation.loginRoute
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.navigation.MsNavHost
import me.injent.myschool.navigation.RootDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MsApp(
    authStatus: AuthStatus,
    update: Update?,
    appState: MsAppState = rememberMsAppState(),
    onUpdateRequest: () -> Unit
) {
    var showUpdateDialog by remember { mutableStateOf(false) }
    LaunchedEffect(update) {
        showUpdateDialog = update != null
    }

    if (showUpdateDialog) {
        update?.let {
            UpdateDialog(
                update = it,
                onUpdateRequest = onUpdateRequest,
                onDismiss = { showUpdateDialog = false }
            )
        }
    }

    val showNetworkError by remember(authStatus) {
        mutableStateOf(
            when (authStatus) {
                is AuthStatus.Error, AuthStatus.Offline -> true
                else -> false
            }
        )
    }

    MsBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                Column(modifier = Modifier.navigationBarsPadding()) {
                    if (appState.shouldShowBottomNavigation) {
                        MsBottomNavigation(
                            destinations = appState.rootDestinations,
                            onNavigate = appState::navigateTo,
                            currentDestination = appState.currentDestination,
                        )
                    }

                    AnimatedVisibility(
                        visible = showNetworkError,
                        enter = expandVertically(tween(1000)),
                        exit = shrinkVertically(tween(1000))
                    ) {
                        Text(
                            text = stringResource(R.string.network_error),
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(vertical = 2.dp)
                        )
                    }
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
                    startDestination = if (authStatus is AuthStatus.NotAuthed || authStatus is AuthStatus.Error) {
                        loginRoute
                    } else {
                        dashboardRoute
                    }
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