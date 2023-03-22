package me.injent.myschool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.injent.myschool.feature.authorization.navigation.authorizationRoute
import me.injent.myschool.feature.authorization.navigation.authorizationScreen
import me.injent.myschool.feature.dashboard.navigation.dashboardScreen
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.leaderboard.navigation.leaderBoardScreen
import me.injent.myschool.feature.leaderboard.navigation.navigateToLeaderboard
import me.injent.myschool.feature.personmarks.navigation.navigateToProfile
import me.injent.myschool.feature.personmarks.navigation.personMarksScreen
import me.injent.myschool.feature.profile.navigation.profileScreen
import me.injent.myschool.feature.statistics.navigation.statisticsScreen
import me.injent.myschool.feature.students.navigation.myClassGraph

@Composable
fun MsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = authorizationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authorizationScreen(onAuthorization = {
            navController.navigateToDashboard(null)
            navController.clearBackStack(authorizationRoute)
        })
        dashboardScreen()
        myClassGraph(
            onPersonClick = { navController.navigateToProfile(it) }
        ) {
            leaderBoardScreen(
                onBack = { navController.popBackStack() }
            )
            personMarksScreen(
                onBack = { navController.popBackStack() },
                onLeaderboardClick = { navController.navigateToLeaderboard(it) }
            )
        }
        statisticsScreen()
        profileScreen()
    }
}