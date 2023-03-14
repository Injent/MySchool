package me.injent.myschool.feature.leaderboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import me.injent.myschool.feature.leaderboard.LeaderboardRoute

const val LEADERBOARD_ROUTE = "leaderboard_route"
const val SUBJECT_ID = "subjectId"

fun NavController.navigateToLeaderboard(subjectId: Long) {
    navigate("$LEADERBOARD_ROUTE/$subjectId")
}

fun NavGraphBuilder.leaderBoardScreen(onBack: () -> Unit) {
    composable(
        route = "$LEADERBOARD_ROUTE/{$SUBJECT_ID}",
        arguments = listOf(
            navArgument(SUBJECT_ID) {
                type = NavType.LongType
            }
        )
    ) {
        LeaderboardRoute(onBack = onBack)
    }
}