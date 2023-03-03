package me.injent.myschool.feature.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val personRepository: PersonRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
}