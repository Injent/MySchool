package me.injent.myschool.feature.authorization.navigation

import android.content.Intent
import androidx.navigation.*
import androidx.navigation.compose.composable
import me.injent.myschool.feature.authorization.AuthorizationRoute
import me.injent.myschool.feature.authorization.BuildConfig

const val AUTHORIZATION_ROUTE = "authorization_route"
const val REDIRECT_URL = "http://myschool"
const val AUTH_URL = "https://login.dnevnik.ru/oauth2?response_type=token" +
        "&client_id=bb97b3e445a340b9b9cab4b9ea0dbd6f" +
        "&scope=CommonInfo,ContactInfo,FriendsAndRelatives,EducationalInfo" +
        "&redirect_uri=$REDIRECT_URL"

fun NavController.navigateToAuthorization() {
    navigate(AUTHORIZATION_ROUTE)
}

fun NavGraphBuilder.authorizationScreen(onAuthorization: () -> Unit) {
    composable(
        route = AUTHORIZATION_ROUTE,
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$REDIRECT_URL/#access_token={token}&state="
                action = Intent.ACTION_VIEW
            }
        ),
        arguments = listOf(
            navArgument("token") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        AuthorizationRoute(onAuthorization = onAuthorization)
    }
}