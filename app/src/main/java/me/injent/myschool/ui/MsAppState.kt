package me.injent.myschool.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.profile.navigation.navigateToProfile
import me.injent.myschool.feature.students.navigation.myClassGraphRoutePattern
import me.injent.myschool.feature.students.navigation.myClassRoute
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

    private val currentRootDestination: RootDestination?
        @Composable get() = when {
            currentDestination?.route == dashboardRoute -> RootDestination.DASHBOARD
            currentDestination?.route?.startsWith(myClassGraphRoutePattern)
                ?: false -> RootDestination.MYCLASS
            else -> null
        }

    val shouldShowBottomNavigation: Boolean
        @Composable get() = currentDestination.isChildOfRootDestination(currentRootDestination)

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
            RootDestination.PROFILE -> navController.navigateToProfile(rootNavOptions)
            RootDestination.STATISTICS -> {}
        }
    }
}

private fun NavDestination?.isDestinationWithBottomNavigation(): Boolean =
    when (this?.route) {
        dashboardRoute, myClassRoute -> true
        else -> false
    }