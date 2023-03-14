package me.injent.myschool.feature.personmarks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.SubjectRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.alias.SingleSubjectAndMarks
import me.injent.myschool.core.model.alias.SubjectsToMarks
import me.injent.myschool.feature.personmarks.navigation.PERSON_ID
import javax.inject.Inject

@HiltViewModel
class PersonMarksViewModel @Inject constructor(
    private val markRepository: MarkRepository,
    personRepository: PersonRepository,
    subjectRepository: SubjectRepository,
    userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val personId: Long = savedStateHandle[PERSON_ID] ?: throw RuntimeException()

    val personMarksUiState: StateFlow<PersonMarksUiState> = combine(
        userDataRepository.userData,
        subjectRepository.subjects
    ) { userData, subjects ->
        val filteredSubjects = subjects.filterNot { userData.bannedSubjects.contains(it) }

        val subjectsToMarks = mutableListOf<SingleSubjectAndMarks>()
        for (subject in filteredSubjects) {
            val marks = markRepository.getPersonMarksBySubject(personId, subject.id).first()
            if (marks.isNotEmpty())
                subjectsToMarks.add(Pair(subject, marks))
        }
        return@combine PersonMarksUiState.Success(subjectsToMarks)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PersonMarksUiState.Loading
        )

    val personUiState: StateFlow<PersonUiState> = personRepository.getPerson(personId)
        .map {
            if (it != null)
                PersonUiState.Success(it)
            else
                PersonUiState.Error

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PersonUiState.Loading
        )
}

sealed interface PersonMarksUiState {
    object Loading : PersonMarksUiState
    object Error : PersonMarksUiState
    data class Success(val subjectsToMarks: SubjectsToMarks) : PersonMarksUiState
}

sealed interface PersonUiState {
    object Loading : PersonUiState
    object Error : PersonUiState
    data class Success(val person: Person) : PersonUiState
}