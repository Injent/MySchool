package me.injent.myschool.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import me.injent.myschool.R
import me.injent.myschool.core.designsystem.icon.MsIcons
import me.injent.myschool.feature.dashboard.navigation.dashboardRoute
import me.injent.myschool.feature.profile.navigation.profileRoute
import me.injent.myschool.feature.statistics.navigation.statisticsRoute
import me.injent.myschool.feature.students.navigation.myClassGraphRoutePattern
import me.injent.myschool.feature.students.navigation.myClassRoute

/**
 * Implementation of the root screens of the application, which has the basic data for the
 * navigation bar. Root screen may contains another screens optionally. When creating another root
 * screen, you must specify its data in this enum class.
 */
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
        route = statisticsRoute
    ),
    PROFILE(
        titleTextId = R.string.profile,
        selectedIcon = MsIcons.ProfileSelected,
        unselectedIcon = MsIcons.Profile,
        route = profileRoute
    )
}