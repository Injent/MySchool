package me.injent.myschool.feature.auth.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.auth.LoginRoute

const val loginRoute = "login_route"
const val ARG_TOKEN = "token"

fun NavController.navigateToLogin() {
    this.navigate(loginRoute)
}

fun NavGraphBuilder.loginScreen(onLogin: () -> Unit) {
    composable(
        route = loginRoute,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${me.injent.myschool.core.auth.BuildConfig.REDIRECT_URI}/#access_token={$ARG_TOKEN}&state="
            }
        ),
        arguments = listOf(
            navArgument(ARG_TOKEN) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        LoginRoute(onLogin = onLogin)
    }
}