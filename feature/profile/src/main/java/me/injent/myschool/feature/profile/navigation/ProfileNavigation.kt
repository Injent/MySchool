package me.injent.myschool.feature.profile.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.profile.ProfileRoute

const val PROFILE_ROUTE = "profile_route"
const val PERSON_ID = "personId"

fun NavController.navigateToProfile(personId: Long) {
    navigate("$PROFILE_ROUTE/$personId")
}

fun NavGraphBuilder.profileScreen() {
    composable(
        route = "$PROFILE_ROUTE/{$PERSON_ID}",
        arguments = listOf(
            navArgument(PERSON_ID) {
                type = NavType.LongType
            }
        )
    ) {
        ProfileRoute()
    }
}