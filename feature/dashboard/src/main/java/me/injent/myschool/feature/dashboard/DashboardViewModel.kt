package me.injent.myschool.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.HomeworkRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.ui.HomeworkUiState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    homeworkRepository: HomeworkRepository
) : ViewModel() {

    val homeworkUiState: StateFlow<HomeworkUiState> = homeworkRepository.homeworkToday
        .map { homeworks ->
            if (homeworks.isNotEmpty()) {
                HomeworkUiState.Success(homeworks)
            } else {
                HomeworkUiState.Empty
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeworkUiState.Loading
        )
}