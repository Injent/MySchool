package me.injent.myschool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.injent.myschool.feature.accounts.navigation.accountsScreen
import me.injent.myschool.feature.auth.navigation.loginRoute
import me.injent.myschool.feature.auth.navigation.loginScreen
import me.injent.myschool.feature.auth.navigation.navigateToLogin
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.feature.dashboard.navigation.dashboardScreen
import me.injent.myschool.feature.dashboard.navigation.navigateToDashboard
import me.injent.myschool.feature.leaderboard.navigation.leaderBoardScreen
import me.injent.myschool.feature.leaderboard.navigation.navigateToLeaderboard
import me.injent.myschool.feature.markpage.navigation.markDetailsScreen
import me.injent.myschool.feature.markpage.navigation.navigateToMarkDetails
import me.injent.myschool.feature.personmarks.navigation.navigateToPersonMarks
import me.injent.myschool.feature.personmarks.navigation.personMarksScreen
import me.injent.myschool.feature.statistics.navigation.statisticsScreen
import me.injent.myschool.feature.students.navigation.myClassGraph

@Composable
fun MsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = dashboardRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(onLogin = {
            navController.navigate(dashboardRoute) {
                popUpTo(loginRoute) {
                    inclusive = true
                }

                launchSingleTop = true
                restoreState = false
            }
        })

        accountsScreen(
            onLogin = {
                navController.navigateToDashboard(null)
            }
        )
        dashboardScreen(
            onMarkClick = { markId ->
                navController.navigateToMarkDetails(markId)
            },
            onLogout = {
                navController.navigate(loginRoute) {
                    popUpTo(dashboardRoute) {
                        inclusive = true
                        saveState = false
                    }
                }
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
        //profileScreen(onLogout = { navController.navigateToAccounts() })

        markDetailsScreen(onBack = { navController.popBackStack() })
    }
}