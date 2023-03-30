package me.injent.myschool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import me.injent.myschool.feature.authorization.navigation.authorizationRoute
import me.injent.myschool.feature.authorization.navigation.authorizationScreen
import me.injent.myschool.feature.dashboard.navigation.dashboardScreen
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.leaderboard.navigation.leaderBoardScreen
import me.injent.myschool.feature.leaderboard.navigation.navigateToLeaderboard
import me.injent.myschool.feature.markpage.navigation.markDetailsScreen
import me.injent.myschool.feature.markpage.navigation.navigateToMarkDetails
import me.injent.myschool.feature.personmarks.navigation.navigateToPersonMarks
import me.injent.myschool.feature.personmarks.navigation.personMarksScreen
import me.injent.myschool.feature.profile.navigation.profileScreen
import me.injent.myschool.feature.statistics.navigation.statisticsScreen
import me.injent.myschool.feature.students.navigation.myClassGraph
import me.injent.myschool.feature.usersearch.navigation.userProfileScreen

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
            navController.navigateToDashboard(navOptions { popUpTo(0) })
        })

        dashboardScreen(
            onMarkClick = { markId ->
                navController.navigateToMarkDetails(markId)
            }
        )
        myClassGraph(
            onPersonClick = { personId -> navController.navigateToPersonMarks(personId) }
        ) {
            leaderBoardScreen(
                onBack = { navController.popBackStack() }
            )
            personMarksScreen(
                onBack = { navController.popBackStack() },
                onLeaderboardClick = { navController.navigateToLeaderboard(it) },
                onMarkClick = { markId -> navController.navigateToMarkDetails(markId) }
            )
        }
        statisticsScreen()
        profileScreen()

        markDetailsScreen(onBack = { navController.popBackStack() })
//        userProfileScreen()
    }
}