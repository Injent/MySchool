package me.injent.myschool.feature.profile.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.profile.ProfileRoute

const val PROFILE_ROUTE = "profile_route"

fun NavController.navigateToProfile() {
    navigate(PROFILE_ROUTE)
}

fun NavGraphBuilder.authorizationScreen() {
    composable(
        route = PROFILE_ROUTE
    ) {
        ProfileRoute()
    }
}