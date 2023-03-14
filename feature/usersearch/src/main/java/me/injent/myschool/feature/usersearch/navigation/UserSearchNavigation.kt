package me.injent.myschool.feature.usersearch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.injent.myschool.feature.usersearch.UserSearchRoute

const val USER_SEARCH_ROUTE = "user_search_route"

fun NavController.navigatoToUserSearch() {
    navigate(USER_SEARCH_ROUTE)
}

fun NavGraphBuilder.userSearchScreen() {
    composable(
        route = USER_SEARCH_ROUTE
    ) {
        UserSearchRoute()
    }
}