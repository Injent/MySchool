package me.injent.myschool.feature.usersearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.data.repository.*
import me.injent.myschool.core.data.repository.remote.ReportingPeriodRepository
import me.injent.myschool.core.model.Group
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.RecentMarks
import me.injent.myschool.feature.usersearch.navigation.USER_ID
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    personRepository: PersonRepository,
    groupRepository: GroupRepository,
    markRepository: MarkRepository,
    userContextRepository: UserContextRepository,
    periodRepository: ReportingPeriodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val userId: Long = savedStateHandle[USER_ID] ?: throw RuntimeException()

    val userProfileUiState = userProfileUiState(
        personRepository = personRepository,
        groupRepository = groupRepository,
        markRepository = markRepository,
        userContextRepository = userContextRepository,
        periodRepository = periodRepository,
        userId = userId,
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserProfileUiState.Loading
        )

}

sealed interface UserProfileUiState {
    object Loading : UserProfileUiState
    data class Success(
        val person: Person,
        val recentMarks: RecentMarks
    ) : UserProfileUiState
    object Error : UserProfileUiState
}

private fun userProfileUiState(
    personRepository: PersonRepository,
    groupRepository: GroupRepository,
    markRepository: MarkRepository,
    userContextRepository: UserContextRepository,
    periodRepository: ReportingPeriodRepository,
    userId: Long
): Flow<UserProfileUiState> {
    return combine(
        personRepository.getPersonByUserId(userId),
        userContextRepository.userContext
    ) { person, userContext ->
        if (person != null && userContext != null) {
            val groupId = groupRepository.getPersonGroups(person.personId)
                .find { it.type == Group.Type.Group }!!.id

            UserProfileUiState.Success(
                person = person,
                recentMarks = markRepository.getRecentMarks(
                    personId = person.personId,
                    groupId = groupId,
                    fromDate = periodRepository.getReportingPeriods(groupId)
                        .find { period ->
                            with(LocalDateTime.currentLocalDateTime().atTimeZone(TimeZone.UTC)) {
                                period.dateStart <= this && period.dateFinish >= this
                            }
                        }?.dateStart
                )
            )
        } else {
            UserProfileUiState.Error
        }
    }
}