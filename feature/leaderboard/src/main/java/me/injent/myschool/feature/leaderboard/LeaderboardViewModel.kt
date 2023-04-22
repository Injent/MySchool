package me.injent.myschool.feature.leaderboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.common.util.MarkDataCounter
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.SubjectRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.PersonAndMarkValue
import me.injent.myschool.feature.leaderboard.navigation.SUBJECT_ID
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    personRepository: PersonRepository,
    markRepository: MarkRepository,
    subjectRepository: SubjectRepository,
    userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val leaderboardUiState: StateFlow<LeaderboardUiState> = leaderboardUiState(
        subjectId = savedStateHandle[SUBJECT_ID] ?: throw RuntimeException(),
        personRepository = personRepository,
        markRepository = markRepository,
        subjectRepository = subjectRepository,
        userDataRepository = userDataRepository
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
    subjectRepository: SubjectRepository,
    userDataRepository: UserDataRepository
) : Flow<LeaderboardUiState> {
    val subjectStream = subjectRepository.getSubject(subjectId)
    val personsStream = personRepository.persons

    return combine(
        subjectStream,
        personsStream,
        userDataRepository.userData
    ) { subject, persons, userData ->
        val period = userData.selectedPeriod!!
        val personsAndMarkValues = persons
            .map { person ->
                val mark = markRepository.getPersonFinalMarkBySubject(
                    person.personId,
                    subjectId,
                    period
                )
                PersonAndMarkValue(
                    personId = person.personId,
                    personName = person.shortName,
                    avatarUrl = person.avatarUrl,
                    value = mark
                )
            }

        val minMark = personsAndMarkValues.minOf(PersonAndMarkValue::value)
        val maxMark = personsAndMarkValues.maxOf(PersonAndMarkValue::value)
        LeaderboardUiState.Success(
            subject.name,
            personsAndMarkValues.toList().sortedByDescending(PersonAndMarkValue::value),
            MarkDataCounter(minMark, maxMark)
        )
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