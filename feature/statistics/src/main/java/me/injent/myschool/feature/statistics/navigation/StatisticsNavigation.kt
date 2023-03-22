package me.injent.myschool.feature.statistics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import me.injent.myschool.feature.statistics.StatisticsRoute

const val statisticsRoute = "statistics_route"

fun NavController.navigateToStatistics(navOptions: NavOptions?) {
    this.navigate(statisticsRoute, navOptions)
}

fun NavGraphBuilder.statisticsScreen() {
    composable(route = statisticsRoute) {
        StatisticsRoute()
    }
}