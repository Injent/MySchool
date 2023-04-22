package me.injent.myschool.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.StatisticRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.Period
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Subject
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    statisticRepository: StatisticRepository,
    userContextRepository: UserContextRepository,
    userDataRepository: UserDataRepository
) : ViewModel() {

    private val _statisticsUiState = MutableStateFlow(StatisticsUiState())
    val statisticsUiState: StateFlow<StatisticsUiState>
        get() = _statisticsUiState.asStateFlow()

    val bestStudentsBySubject = bestStudentsBySubject(
        statisticRepository, userDataRepository, _statisticsUiState
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyMap()
        )

    fun setPeriod(period: Period) {
        _statisticsUiState.update {
            it.copy(selectedPeriod = period)
        }
    }
}

data class StatisticsUiState(
    val selectedPeriod: Period? = null
)

private fun bestStudentsBySubject(
    statisticRepository: StatisticRepository,
    userDataRepository: UserDataRepository,
    uiState: MutableStateFlow<StatisticsUiState>
): Flow<Map<Subject, Person>> {
    val dataFlow = userDataRepository.userData
    return dataFlow.flatMapLatest { userData ->
        uiState.update {
            it.copy(selectedPeriod = userData.selectedPeriod!!)
        }
        statisticRepository.getBestStudentsBySubject(userData.selectedPeriod!!)
    }
        .map {
            it.toList().sortedBy { (subject, _) -> subject.name }.toMap()
        }
}