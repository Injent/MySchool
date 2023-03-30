package me.injent.myschool.feature.personmarks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Subject
import me.injent.myschool.feature.personmarks.navigation.PERSON_ID
import javax.inject.Inject

@HiltViewModel
class PersonMarksViewModel @Inject constructor(
    private val markRepository: MarkRepository,
    personRepository: PersonRepository,
    subjectRepository: SubjectRepository,
    userDataRepository: UserDataRepository,
    contextRepository: UserContextRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val personId: Long = savedStateHandle[PERSON_ID] ?: throw RuntimeException()

    val personMarksUiState: StateFlow<PersonMarksUiState> = personMarksUiState(
        personId = personId,
        subjectRepository = subjectRepository,
        markRepository = markRepository,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PersonMarksUiState.Loading
        )

    val personUiState: StateFlow<PersonUiState> = combine(
        personRepository.getPerson(personId),
        contextRepository.userContext
    ) { person, userContext ->
        if (person != null && userContext != null)
            PersonUiState.Success(
                person = person,
                isMe = personId == userContext.personId
            )
        else
            PersonUiState.Error
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PersonUiState.Loading
        )
}

sealed interface PersonUiState {
    object Loading : PersonUiState
    object Error : PersonUiState
    data class Success(
        val person: Person,
        val isMe: Boolean
    ) : PersonUiState
}

sealed interface PersonMarksUiState {
    object Loading : PersonMarksUiState
    object Error : PersonMarksUiState
    data class Success(
        val subjectsToMarks: Map<Subject, List<Mark>>
    ) : PersonMarksUiState
}

private fun personMarksUiState(
    personId: Long,
    subjectRepository: SubjectRepository,
    markRepository: MarkRepository
): Flow<PersonMarksUiState> {
    return subjectRepository.subjects
        .map { subjects ->
            val subjectToMarks = subjects.associateWith { subject ->
                markRepository.getPersonMarksBySubject(personId, subject.id)
            }.filter { (_, marks) -> marks.isNotEmpty() }

            PersonMarksUiState.Success(
                subjectToMarks
            )
        }
}