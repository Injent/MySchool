package me.injent.myschool.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.core.data.repository.DnevnikRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.datastore.toDomainModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dnevnikRepository: DnevnikRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
}