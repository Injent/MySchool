package me.injent.myschool.feature.profile

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
import me.injent.myschool.core.model.alias.SubjectsAndMarks
import me.injent.myschool.feature.profile.navigation.PERSON_ID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val markRepository: MarkRepository,
    private val personRepository: PersonRepository,
    private val subjectRepository: SubjectRepository,
    userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val personId: Long = savedStateHandle[PERSON_ID] ?: throw RuntimeException()

//    val state = personRepository.getPersonsStream()
//        .map { persons ->
//            persons.map { person ->
//                val mark = markRepository.getPersonFinalMarkBySubject(person.personId, subjectId)
//                MarkValueAndPerson(person.personId, person.shortName, mark)
//            }
//        }
//        .map { it.sortedByDescending(MarkValueAndPerson::value) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = emptyList()
//        )

    val subjectTableUiState: StateFlow<SubjectsAndMarks> = combine(
        userDataRepository.userData,
        subjectRepository.subjects
    ) { userData, subjects ->
        val filteredSubjects = subjects.filterNot { userData.bannedSubjects.contains(it.id) }

        val subjectAndMarks = mutableListOf<SingleSubjectAndMarks>()
        for (subject in filteredSubjects) {
            val marks = markRepository.getPersonMarksBySubject(personId, subject.id).first()
            if (marks.isNotEmpty())
                subjectAndMarks.add(Pair(subject, marks))
        }
        return@combine subjectAndMarks.toList()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
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

sealed interface PersonUiState {
    object Loading : PersonUiState
    object Error : PersonUiState
    data class Success(val person: Person) : PersonUiState
}