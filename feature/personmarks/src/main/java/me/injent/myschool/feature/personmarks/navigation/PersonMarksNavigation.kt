package me.injent.myschool.feature.personmarks.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.personmarks.ProfileRoute

const val PERSON_MARKS_ROUTE = "person_marks_route"
const val PERSON_ID = "personId"

fun NavController.navigateToProfile(personId: Long) {
    navigate("$PERSON_MARKS_ROUTE/$personId")
}

fun NavGraphBuilder.profileScreen(
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit
) {
    composable(
        route = "$PERSON_MARKS_ROUTE/{$PERSON_ID}",
        arguments = listOf(
            navArgument(PERSON_ID) {
                type = NavType.LongType
            }
        )
    ) {
        ProfileRoute(
            onBack = onBack,
            onLeaderboardClick = onLeaderboardClick
        )
    }
}