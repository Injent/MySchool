package me.injent.myschool.feature.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.model.MarkValueAndPerson
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val markRepository: MarkRepository
) : ViewModel() {
//    val state = markRepository.getPersonsTopMarksStream(1975457915877178846)
//        .map { it.sortedByDescending(MarkValueAndPerson::value) }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5_000),
//            emptyList()
//        )
}

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val list: List<MarkValueAndPerson> = emptyList()) : ProfileUiState
}