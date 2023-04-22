package me.injent.myschool.feature.markpage.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
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
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "myschool://mark/id={$MARK_ID}"
            }
        )
    ) {
        MarkDetailsRoute(
            onBack = onBack
        )
    }
}