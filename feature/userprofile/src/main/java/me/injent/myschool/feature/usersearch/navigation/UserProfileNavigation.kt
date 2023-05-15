package me.injent.myschool.feature.usersearch.navigation

import android.content.Intent
import androidx.navigation.*
import androidx.navigation.compose.composable

const val userProfileRoute = "profile_user_route"
internal const val USER_ID = "userId"

fun NavController.navigateToUserProfile(userId: Long) {
    navigate("$userProfileRoute/{$userId}")
}

fun NavGraphBuilder.userProfileScreen() {
    composable(
        route = userProfileRoute,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "https://dnevnik.ru/user/user.aspx?user={$USER_ID}"
                action = Intent.ACTION_VIEW
            }
        ),
        arguments = listOf(
            navArgument(USER_ID) {
                type = NavType.LongType
            }
        )
    ) {

    }
}