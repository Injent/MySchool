package me.injent.myschool.feature.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.datetime.LocalDateTime
import me.injent.myschool.core.common.util.currentDateTimeAtStartOfDay
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.repository.remote.UserFeedRepository
import me.injent.myschool.core.model.UserFeed
import javax.inject.Inject

private const val BIRTHDAYS_LIMIT = 3

@HiltViewModel
class DashboardViewModel @Inject constructor(
    personRepository: PersonRepository,
    userContextRepository: UserContextRepository,
    userFeedRepository: UserFeedRepository
) : ViewModel() {

    val feedUiState: StateFlow<FeedUiState> = feedUiState(
        userFeedRepository = userFeedRepository,
        userContextRepository = userContextRepository
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = FeedUiState.Loading
        )

    val birthdaysUiState = personRepository.getClosestBirthdays(BIRTHDAYS_LIMIT)
        .map {
            BirthdaysUiState.Success(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BirthdaysUiState.Loading
        )

    val myName: StateFlow<String> = userContextRepository.userContext
        .map { context ->
            context?.firstName ?: ""
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: PointEvent) {
        when (event) {
            is PointEvent.GoToMarkDetails -> {
                _uiState.update {
                    it.copy(viewingMarkDetails = event.markId)
                }
            }
            is PointEvent.OpenHomeworkDialog -> {
                _uiState.update {
                    it.copy(viewingHomework = event.homework)
                }
            }
            PointEvent.CloseHomeworkDialog -> {
                _uiState.update { it.copy(viewingHomework = null) }
            }
            PointEvent.BackEvent -> {
                _uiState.update {
                    it.copy(viewingMarkDetails = null)
                }
            }
        }
    }
}

data class DashboardUiState(
    val viewingHomework: UserFeed.Homework? = null,
    val viewingMarkDetails: Long? = null
)

private const val DAY_INTERVAL = 10

private fun feedUiState(
    userFeedRepository: UserFeedRepository,
    userContextRepository: UserContextRepository
): Flow<FeedUiState> = flow {
    val userFeed = userFeedRepository.getUserFeed(
        date = LocalDateTime.currentDateTimeAtStartOfDay(),
        personId = userContextRepository.userContext.first()!!.personId,
        limit = DAY_INTERVAL
    )

    emit(FeedUiState.Success(
        todayHomeworks = userFeed.currentDay?.todayHomeworks ?: emptyList(),
        todaySchedule = userFeed.currentDay?.todaySchedule ?: emptyList(),
        marksCards = userFeed.days
            .flatMap { it.marksCards }
            .filter { it.marks.isNotEmpty() }
    ))
}