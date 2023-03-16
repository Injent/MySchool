package me.injent.myschool.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import me.injent.myschool.R
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.feature.profile.navigation.profileRoute
import me.injent.myschool.feature.students.navigation.myClassGraphRoutePattern

enum class RootDestination(
    @StringRes val titleTextId: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String
) {
    DASHBOARD(
        titleTextId = R.string.dashboard,
        selectedIcon = MsIcons.DashboardSelected,
        unselectedIcon = MsIcons.Dashboard,
        route = dashboardRoute
    ),
    MYCLASS(
        titleTextId = R.string.my_class,
        selectedIcon = MsIcons.MyClassSelected,
        unselectedIcon = MsIcons.MyClass,
        route = myClassGraphRoutePattern
    ),
    STATISTICS(
        titleTextId = R.string.statistics,
        selectedIcon = MsIcons.Statistics,
        unselectedIcon = MsIcons.Statistics,
        route = ""
    ),
    PROFILE(
        titleTextId = R.string.profile,
        selectedIcon = MsIcons.ProfileSelected,
        unselectedIcon = MsIcons.Profile,
        route = profileRoute
    )
}