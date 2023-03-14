package me.injent.myschool.feature.students.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import me.injent.myschool.feature.students.MyClassRoute

const val MY_CLASS_ROUTE = "myclass_route"

fun NavController.navigateToMyClass(navOptions: NavOptions?) {
    navigate(MY_CLASS_ROUTE, navOptions)
}

fun NavGraphBuilder.myClassScreeen(onPersonClick: (personId: Long) -> Unit) {
    composable(
        route = MY_CLASS_ROUTE
    ) {
        MyClassRoute(
            onPersonClick = onPersonClick
        )
    }
}