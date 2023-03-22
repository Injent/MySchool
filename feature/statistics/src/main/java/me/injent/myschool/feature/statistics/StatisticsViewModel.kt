package me.injent.myschool.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.StatisticRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    statisticRepository: StatisticRepository,
    contextRepository: UserContextRepository
) : ViewModel() {

    val averageMarks = statisticsUiState(
        contextRepository,
        statisticRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = StatisticsUiState.Loading
        )
}

sealed interface StatisticsUiState {
    object Loading : StatisticsUiState
    data class Success(
        val averageMarksByWeek: List<Float>
    ) : StatisticsUiState
    object Error : StatisticsUiState
}

private fun statisticsUiState(
    contextRepository: UserContextRepository,
    statisticRepository: StatisticRepository
): Flow<StatisticsUiState> {
    return contextRepository.userContext
        .map { userContext ->
            if (userContext == null)
                return@map StatisticsUiState.Error
            val averagemarksByWeek =
                statisticRepository.getAverageMarksByWeek(userContext.personId)
            return@map StatisticsUiState.Success(averagemarksByWeek)
        }
}