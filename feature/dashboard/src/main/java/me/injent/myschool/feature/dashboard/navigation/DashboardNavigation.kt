package me.injent.myschool.feature.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import me.injent.myschool.feature.dashboard.DashboardRoute

const val dashboardRoute = "dashboard_route"

fun NavController.navigateToDashboard(navOptions: NavOptions?) {
    navigate(dashboardRoute, navOptions)
}

fun NavGraphBuilder.dashboardScreen(
    onMarkClick: (markId: Long) -> Unit,
    onLogout: () -> Unit,
) {
    composable(route = dashboardRoute) {
        DashboardRoute(
            onMarkClick = onMarkClick,
            onLogout = onLogout
        )
    }
}