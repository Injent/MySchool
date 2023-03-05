package me.injent.myschool.feature.students.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import me.injent.myschool.feature.students.StudentsRoute

const val STUDENTS_ROUTE = "students_route"

fun NavController.navigateToStudents() {
    navigate(STUDENTS_ROUTE)
}

fun NavGraphBuilder.studentsScreen(onPersonClick: (personId: Long) -> Unit) {
    composable(
        route = STUDENTS_ROUTE
    ) {
        StudentsRoute(
            onPersonClick = onPersonClick
        )
    }
}