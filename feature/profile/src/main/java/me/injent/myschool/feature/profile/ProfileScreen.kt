package me.injent.myschool.feature.profile

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel()
) {

    ProfileScreen()
}

@Composable
internal fun ProfileScreen() {

}