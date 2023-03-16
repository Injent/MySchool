package me.injent.myschool.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.model.UserContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userContextRepository: UserContextRepository
) : ViewModel() {

    val profileUiState: StateFlow<ProfileUiState> = userContextRepository.userContext
        .map {
            if (it != null)
                ProfileUiState.Success(it)
            else
                ProfileUiState.Error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading
        )
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    object Error : ProfileUiState
    data class Success(val profile: UserContext) : ProfileUiState
}