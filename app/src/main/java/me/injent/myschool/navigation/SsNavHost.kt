package me.injent.myschool.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.injent.myschool.feature.authorization.navigation.AUTHORIZATION_ROUTE
import me.injent.myschool.feature.authorization.navigation.authorizationScreen

@Composable
fun MsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AUTHORIZATION_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authorizationScreen(onAuthorization = { navController.navigate("A") })
        composable("A") {
            Box(modifier = Modifier.fillMaxSize().background(Color.Magenta))
        }
    }
}