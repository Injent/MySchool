package me.injent.myschool.feature.markpage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import me.injent.myschool.feature.markpage.MarkDetailsRoute

const val markDetailsRoute = "mark_details_route"
const val MARK_ID = "markId"

fun NavController.navigateToMarkDetails(markId: Long) {
    this.navigate("$markDetailsRoute/$markId")
}

fun NavGraphBuilder.markDetailsScreen(
    onBack: () -> Unit
) {
    composable(
        route = "$markDetailsRoute/{$MARK_ID}",
        arguments = listOf(
            navArgument(MARK_ID) {
                type = NavType.LongType
            }
        )
    ) {
        MarkDetailsRoute(
            onBack = onBack
        )
    }
}