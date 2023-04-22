package me.injent.myschool.feature.accounts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.injent.myschool.feature.accounts.AccountsRoute

const val accountsRoute = "accountsRoute"

fun NavController.navigateToAccounts() {
    this.navigate(accountsRoute)
}

fun NavGraphBuilder.accountsScreen(onLogin: () -> Unit) {
    composable(
        route = accountsRoute
    ) {
        AccountsRoute(
            onLogin = onLogin
        )
    }
}