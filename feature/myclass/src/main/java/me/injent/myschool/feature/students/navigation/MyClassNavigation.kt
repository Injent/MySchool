package me.injent.myschool.feature.students.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import me.injent.myschool.feature.students.MyClassRoute

const val myClassGraphRoutePattern = "myclass_graph"
const val myClassRoute = "myclass_route"

fun NavController.navigateToMyClass(navOptions: NavOptions?) {
    navigate(myClassGraphRoutePattern, navOptions)
}

fun NavGraphBuilder.myClassGraph(
    onPersonClick: (personId: Long) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = myClassGraphRoutePattern,
        startDestination = myClassRoute
    ) {
        composable(route = myClassRoute) {
            MyClassRoute(
                onPersonClick = onPersonClick
            )
        }
        nestedGraphs()
    }
}