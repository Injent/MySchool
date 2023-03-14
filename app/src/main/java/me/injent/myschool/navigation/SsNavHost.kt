package me.injent.myschool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.injent.myschool.feature.authorization.navigation.AUTHORIZATION_ROUTE
import me.injent.myschool.feature.authorization.navigation.authorizationScreen
import me.injent.myschool.feature.dashboard.navigation.dashboardScreen
import me.injent.myschool.feature.leaderboard.navigation.leaderBoardScreen
import me.injent.myschool.feature.leaderboard.navigation.navigateToLeaderboard
import me.injent.myschool.feature.personmarks.navigation.navigateToProfile
import me.injent.myschool.feature.personmarks.navigation.profileScreen
import me.injent.myschool.feature.students.navigation.navigateToMyClass
import me.injent.myschool.feature.students.navigation.myClassScreeen

@Composable
fun MsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AUTHORIZATION_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authorizationScreen(onAuthorization = { navController.navigateToMyClass(null) })
        profileScreen(
            onBack = { navController.popBackStack() },
            onLeaderboardClick = { navController.navigateToLeaderboard(it) }
        )
        myClassScreeen(
            onPersonClick = { navController.navigateToProfile(it) }
        )
        dashboardScreen()
        leaderBoardScreen(
            onBack = { navController.popBackStack() }
        )
    }
}