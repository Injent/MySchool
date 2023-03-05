package me.injent.myschool.feature.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.PersonAndMarkValue
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    personRepository: PersonRepository,
    private val markRepository: MarkRepository,
    userDataRepository: UserDataRepository,
) : ViewModel() {
    private val _myPlace = MutableStateFlow(0)
    val myPlace: StateFlow<Int>
        get() = _myPlace.asStateFlow()

    val students: StateFlow<List<PersonAndMarkValue>> = combine(
        personRepository.getPersonsStream(),
        userDataRepository.userData
    ) { persons, userData ->
        val personNamesAndMarks = mutableListOf<PersonAndMarkValue>()
        for (person in persons) {
            val mark = markRepository.getPersonAverageMark(person.personId).first()
            personNamesAndMarks.add(PersonAndMarkValue(person.personId, person.shortName, mark))
        }
        val sortedTopList = personNamesAndMarks.toList().sortedByDescending(PersonAndMarkValue::value)
        val myPlace = sortedTopList.indexOfFirst { it.personId == userData.userContext!!.personId }
        _myPlace.value = myPlace + 1
        return@combine sortedTopList
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}