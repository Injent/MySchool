package me.injent.myschool.feature.leaderboard

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.SubjectRepository
import me.injent.myschool.core.model.PersonAndMarkValue
import me.injent.myschool.feature.leaderboard.navigation.SUBJECT_ID
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.common.result.asResult
import me.injent.myschool.core.common.util.MarkDataCounter
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    personRepository: PersonRepository,
    markRepository: MarkRepository,
    subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val leaderboardUiState: StateFlow<LeaderboardUiState> = leaderboardUiState(
        subjectId = savedStateHandle[SUBJECT_ID] ?: throw RuntimeException(),
        personRepository = personRepository,
        markRepository = markRepository,
        subjectRepository = subjectRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LeaderboardUiState.Loading
        )
}

private fun leaderboardUiState(
    subjectId: Long,
    personRepository: PersonRepository,
    markRepository: MarkRepository,
    subjectRepository: SubjectRepository
) : Flow<LeaderboardUiState> {
    val subjectStream = subjectRepository.getSubject(subjectId)
    val personsStream = personRepository.getPersonsStream()

    return combine(
        subjectStream,
        personsStream,
        ::Pair
    )
        .asResult()
        .map { subjectToPersons ->
            when (subjectToPersons) {
                is Result.Success -> {
                    val personsAndMarkValues = mutableListOf<PersonAndMarkValue>()
                    for (person in subjectToPersons.data.second) {
                        val mark = markRepository
                            .getPersonFinalMarkBySubject(person.personId, subjectId)
                        val personAndMark
                            = PersonAndMarkValue(person.personId, person.shortName, mark)
                        personsAndMarkValues.add(personAndMark)
                    }
                    val minMark = personsAndMarkValues.minOf(PersonAndMarkValue::value)
                    val maxMark = personsAndMarkValues.maxOf(PersonAndMarkValue::value)
                    LeaderboardUiState.Success(
                        subjectToPersons.data.first.name,
                        personsAndMarkValues.toList().sortedByDescending(PersonAndMarkValue::value),
                        MarkDataCounter(minMark, maxMark)
                    )
                }
                Result.Loading -> LeaderboardUiState.Loading
                is Result.Error -> LeaderboardUiState.Error
            }
        }
}

sealed interface LeaderboardUiState {
    data class Success(
        val subjectName: String,
        val personsAndMarks: List<PersonAndMarkValue>,
        val counter: MarkDataCounter
    ) : LeaderboardUiState
    object Error : LeaderboardUiState
    object Loading : LeaderboardUiState
}