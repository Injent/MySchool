package me.injent.myschool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import me.injent.myschool.feature.authorization.navigation.AUTHORIZATION_ROUTE
import me.injent.myschool.feature.authorization.navigation.authorizationScreen
import me.injent.myschool.feature.profile.navigation.navigateToProfile
import me.injent.myschool.feature.profile.navigation.profileScreen
import me.injent.myschool.feature.students.navigation.navigateToStudents
import me.injent.myschool.feature.students.navigation.studentsScreen

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
        authorizationScreen(onAuthorization = { navController.navigateToStudents() })
        profileScreen(
            onBack = { navController.popBackStack() }
        )
        studentsScreen(
            onPersonClick = { navController.navigateToProfile(it) }
        )
    }
}