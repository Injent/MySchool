package me.injent.myschool.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import me.injent.myschool.R
import me.injent.myschool.core.designsystem.icon.MsIcons

enum class RootDestination(
    @StringRes val titleTextId: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    DASHBOARD(
        titleTextId = R.string.dashboard,
        selectedIcon = MsIcons.DashboardSelected,
        unselectedIcon = MsIcons.Dashboard
    ),
    MYCLASS(
        titleTextId = R.string.my_class,
        selectedIcon = MsIcons.MyClassSelected,
        unselectedIcon = MsIcons.MyClass
    ),
    STATISTICS(
        titleTextId = R.string.statistics,
        selectedIcon = MsIcons.Statistics,
        unselectedIcon = MsIcons.Statistics
    ),
    PROFILE(
    titleTextId = R.string.profile,
    selectedIcon = MsIcons.ProfileSelected,
    unselectedIcon = MsIcons.Profile
    )
}