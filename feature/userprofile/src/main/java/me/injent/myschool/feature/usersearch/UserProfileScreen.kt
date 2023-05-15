package me.injent.myschool.feature.usersearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

//@Composable
//internal fun UserProfileRoute(
//    viewModel: UserProfileViewModel = hiltViewModel()
//) {
//    val userProfileUiState by viewModel.collectAsStateWithLifecycle()
//
//    UserProfileScreen(
//        profileUiState = userProfileUiState
//    )
//}

@Composable
private fun UserProfileScreen(
    profileUiState: UserProfileUiState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = if (profileUiState is UserProfileUiState.Success) {
                profileUiState.person.shortName
            } else "",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}