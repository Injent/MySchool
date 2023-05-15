package me.injent.myschool.feature.students

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.domain.GetPersonsWithAverageMark
import me.injent.myschool.core.domain.model.PersonWithAverageMark
import me.injent.myschool.feature.students.model.PeriodChip
import javax.inject.Inject

sealed interface MyClassUiState {
    object Loading : MyClassUiState
    object Error : MyClassUiState
    data class Success(
        val myPlace: Int,
        val personsAndMarks: List<PersonWithAverageMark>,
        val periods: List<PeriodChip>,
        val selectedPeriodNumber: Int
    ) : MyClassUiState
}

@HiltViewModel
class MyClassViewModel @Inject constructor(
    personsWithAverageMarkUseCase: GetPersonsWithAverageMark,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val myClassUiState: StateFlow<MyClassUiState> = myClassUiState(
        personsWithAverageMarkUseCase = personsWithAverageMarkUseCase,
        userDataRepository = userDataRepository,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = MyClassUiState.Loading
        )

    fun selectPeriod(period: PeriodChip) {
        viewModelScope.launch {
            val selectedPeriod = userDataRepository.userData.first().userContext!!
                .reportingPeriodGroup.periods.find { it.number == period.number }!!
            userDataRepository.selectPeriod(selectedPeriod)
        }
    }
}

private fun myClassUiState(
    userDataRepository: UserDataRepository,
    personsWithAverageMarkUseCase: GetPersonsWithAverageMark,
): Flow<MyClassUiState> {
    return userDataRepository.userData.mapLatest { userData ->
        val userContext = userData.userContext!!
        val selectedPeriod = userData.selectedPeriod!!

        val personsTop = personsWithAverageMarkUseCase(period = selectedPeriod)

        val myPersonId = userContext.personId
        val myPlace = personsTop.indexOfFirst { it.personId == myPersonId }

        val periods = userContext.reportingPeriodGroup.periods
            .map { period ->
                PeriodChip(
                    type = period.type,
                    number = period.number,
                    isCurrent = period.isCurrent,
                    dateStart = period.dateStart,
                    dateFinish = period.dateFinish
                )
            }

        MyClassUiState.Success(
            myPlace = myPlace + 1,
            personsAndMarks = personsTop,
            periods = periods,
            selectedPeriodNumber = selectedPeriod.number
        ) as MyClassUiState
    }
        .onStart { emit(MyClassUiState.Loading) }
        .catch { emit(MyClassUiState.Error) }
}