package me.injent.myschool.feature.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.PeriodType
import me.injent.myschool.core.model.PersonAndMarkValue
import javax.inject.Inject

@HiltViewModel
class MyClassViewModel @Inject constructor(
    personRepository: PersonRepository,
    markRepository: MarkRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val myClassUiState: StateFlow<MyClassUiState> = myClassUiState(
        personRepository = personRepository,
        userDataRepository = userDataRepository,
        markRepository = markRepository,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
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

data class PeriodChip(
    val type: PeriodType,
    val number: Int,
    val isCurrent: Boolean,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime
)

sealed interface MyClassUiState {
    object Loading : MyClassUiState
    object Error : MyClassUiState
    data class Success(
        val myPlace: Int,
        val personsAndMarks: List<PersonAndMarkValue>,
        val periods: List<PeriodChip>,
        val selectedPeriodNumber: Int
    ) : MyClassUiState
}

private fun myClassUiState(
    personRepository: PersonRepository,
    userDataRepository: UserDataRepository,
    markRepository: MarkRepository,
): Flow<MyClassUiState> {
    return combine(
        userDataRepository.userData,
        personRepository.persons
    ) { userData, persons ->
        val userContext = userData.userContext!!
        val selectedPeriod = userData.selectedPeriod!!

        val personsTop = persons
            .map { person ->
                val markValue = markRepository.getPersonAverageMarkValue(
                    personId = person.personId,
                    dateStart = selectedPeriod.dateStart,
                    dateFinish = selectedPeriod.dateFinish
                )
                PersonAndMarkValue(
                    personId = person.personId,
                    personName = person.shortName,
                    avatarUrl = person.avatarUrl,
                    value = markValue
                )
            }
            .sortedByDescending(PersonAndMarkValue::value)

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
        )
    }
}