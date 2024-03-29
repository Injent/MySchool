package me.injent.myschool.feature.personmarks.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.personmarks.PersonMarksRoute

const val PERSON_MARKS_ROUTE = "person_marks_route"
const val PERSON_ID = "personId"

fun NavController.navigateToPersonMarks(personId: Long) {
    navigate("$PERSON_MARKS_ROUTE/$personId")
}

fun NavGraphBuilder.personMarksScreen(
    onBack: () -> Unit,
    onLeaderboardClick: (subjectId: Long) -> Unit,
    onMarkClick: (markId: Long) -> Unit
) {
    composable(
        route = "$PERSON_MARKS_ROUTE/{$PERSON_ID}",
        arguments = listOf(
            navArgument(PERSON_ID) {
                type = NavType.LongType
            }
        )
    ) {
        PersonMarksRoute(
            onBack = onBack,
            onLeaderboardClick = onLeaderboardClick,
            onMarkClick = onMarkClick
        )
    }
}