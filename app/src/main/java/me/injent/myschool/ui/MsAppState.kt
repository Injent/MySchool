package me.injent.myschool.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import me.injent.myschool.feature.authorization.navigation.authorizationRoute
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.profile.navigation.navigateToProfile
import me.injent.myschool.feature.profile.navigation.profileRoute
import me.injent.myschool.feature.statistics.navigation.navigateToStatistics
import me.injent.myschool.feature.statistics.navigation.statisticsRoute
import me.injent.myschool.feature.students.navigation.myClassGraphRoutePattern
import me.injent.myschool.feature.students.navigation.myClassRoute
import me.injent.myschool.feature.students.navigation.navigateToMyClass
import me.injent.myschool.navigation.RootDestination

@Composable
fun rememberMsAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
) : MsAppState {
    return remember(coroutineScope, navController) {
        MsAppState(navController, coroutineScope)
    }
}

@Stable
class MsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    val rootDestinations: List<RootDestination>
        get() = RootDestination.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    private val currentRootDestination: RootDestination?
        @Composable get() = with("${currentDestination?.parent?.route ?: ""}/${currentDestination?.route ?: ""}") {
            when {
                contains(dashboardRoute) -> RootDestination.DASHBOARD
                contains(myClassGraphRoutePattern) -> RootDestination.MYCLASS
                contains(statisticsRoute) -> RootDestination.STATISTICS
                contains(profileRoute) -> RootDestination.PROFILE
                else -> null
            }
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
            RootDestination.STATISTICS -> navController.navigateToStatistics(rootNavOptions)
        }
    }
}