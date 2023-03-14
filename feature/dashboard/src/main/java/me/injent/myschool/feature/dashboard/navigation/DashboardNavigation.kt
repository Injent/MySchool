package me.injent.myschool.feature.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import me.injent.myschool.feature.dashboard.DashboardRoute

const val DASHBOARD_ROUTE = "dashboard_route"

fun NavController.navigateToDashboard(navOptions: NavOptions) {
    navigate(DASHBOARD_ROUTE, navOptions)
}

fun NavGraphBuilder.dashboardScreen() {
    composable(
        route = DASHBOARD_ROUTE
    ) {
        DashboardRoute()
    }
}