package me.injent.myschool.feature.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.common.result.Result
import me.injent.myschool.core.common.result.asResult
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.core.model.PersonAndMarkValue
import javax.inject.Inject

@HiltViewModel
class MyClassViewModel @Inject constructor(
    personRepository: PersonRepository,
    markRepository: MarkRepository,
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val myClassUiState: StateFlow<MyClassUiState> = myClassUiState(
        personRepository = personRepository,
        userDataRepository = userDataRepository,
        markRepository = markRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MyClassUiState.Loading
        )
}

private fun myClassUiState(
    personRepository: PersonRepository,
    userDataRepository: UserDataRepository,
    markRepository: MarkRepository
): Flow<MyClassUiState> {
    return combine(
        userDataRepository.userData,
        personRepository.persons,
        ::Pair
    )
        .asResult()
        .map { result ->
            when (result) {
                Result.Loading -> MyClassUiState.Loading
                is Result.Success -> {
                    val myPersonId = result.data.first.userContext!!.personId

                    val personNamesAndMarks = mutableListOf<PersonAndMarkValue>()
                    for (person in result.data.second) {
                        val mark = markRepository.getPersonAverageMarkStream(person.personId).first()
                        personNamesAndMarks.add(
                            PersonAndMarkValue(person.personId, person.shortName, mark)
                        )
                    }

                    val sortedTopList = personNamesAndMarks.sortedByDescending(PersonAndMarkValue::value)
                    val myPlace = sortedTopList.indexOfFirst { it.personId == myPersonId }

                    MyClassUiState.Success(
                        myPlace = myPlace + 1,
                        personsAndMarks = sortedTopList
                    )
                }
                is Result.Error -> MyClassUiState.Error
            }
        }
}