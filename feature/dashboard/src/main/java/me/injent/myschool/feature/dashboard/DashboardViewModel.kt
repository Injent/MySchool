package me.injent.myschool.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.HomeworkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import javax.inject.Inject

private const val BIRTHDAYS_LIMIT = 3

@HiltViewModel
class DashboardViewModel @Inject constructor(
    homeworkRepository: HomeworkRepository,
    personRepository: PersonRepository,
    userContextRepository: UserContextRepository
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
            started = SharingStarted.Eagerly,
            initialValue = HomeworkUiState.Loading
        )

    val birthdaysUiState = personRepository.getClosestBirthdays(BIRTHDAYS_LIMIT)
        .map {
            BirthdaysUiState.Success(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BirthdaysUiState.Loading
        )

    val myName: StateFlow<String> = userContextRepository.userContext
        .map { context ->
            context?.firstName ?: ""
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )
}