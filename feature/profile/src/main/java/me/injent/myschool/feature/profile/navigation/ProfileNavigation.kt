package me.injent.myschool.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import me.injent.myschool.feature.profile.ProfileRoute

const val profileRoute = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions?) {
    this.navigate(profileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(onLogout: () -> Unit) {
    composable(route = profileRoute) {
        ProfileRoute(onLogout = onLogout)
    }
}