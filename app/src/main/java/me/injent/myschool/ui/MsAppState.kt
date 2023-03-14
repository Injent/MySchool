package me.injent.myschool.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import me.injent.myschool.feature.dashboard.navigation.DASHBOARD_ROUTE
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.students.navigation.MY_CLASS_ROUTE
import me.injent.myschool.feature.students.navigation.navigateToMyClass
import me.injent.myschool.navigation.RootDestination

@Composable
fun rememberMsAppState(
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
) : MsAppState {
    return remember(coroutineScope, navController, windowSizeClass) {
        MsAppState(navController, coroutineScope, windowSizeClass)
    }
}

@Stable
class MsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {
    val rootDestinations: List<RootDestination>
        get() = RootDestination.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomNavigation: Boolean
        @Composable get() = currentDestination.isDestinationWithBottomNavigation()

    val currentRootDestination: RootDestination?
        @Composable get() = when (currentDestination?.route) {
            DASHBOARD_ROUTE -> RootDestination.DASHBOARD
            else -> null
        }

    fun navigateTo(destination: RootDestination) {
        val rootNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }

        when (destination) {
            RootDestination.DASHBOARD -> navController.navigateToDashboard(rootNavOptions)
            RootDestination.MYCLASS -> navController.navigateToMyClass(rootNavOptions)
            RootDestination.PROFILE -> {}
            RootDestination.STATISTICS -> {}
        }
    }
}

private fun NavDestination?.isDestinationWithBottomNavigation(): Boolean =
    when (this?.route) {
        DASHBOARD_ROUTE, MY_CLASS_ROUTE -> true
        else -> false
    }