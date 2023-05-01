package me.injent.myschool.feature.auth.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.auth.LoginRoute

const val loginRoute = "login_route"

fun NavController.navigateToLogin() {
    this.navigate(loginRoute)
}

fun NavGraphBuilder.loginScreen(onLogin: () -> Unit) {
    composable(
        route = loginRoute,
    ) {
        LoginRoute(onLogin = onLogin)
    }
}