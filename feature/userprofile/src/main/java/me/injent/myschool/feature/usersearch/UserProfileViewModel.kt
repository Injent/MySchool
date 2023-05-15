package me.injent.myschool.feature.usersearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import me.injent.myschool.core.common.util.atTimeZone
import me.injent.myschool.core.common.util.currentLocalDateTime
import me.injent.myschool.core.data.repository.*
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userId: Long = savedStateHandle[USER_ID] ?: throw RuntimeException()

}

sealed interface UserProfileUiState {
    object Loading : UserProfileUiState
    data class Success(
        val person: Person,
        val recentMarks: RecentMarks
    ) : UserProfileUiState
    object Error : UserProfileUiState
}