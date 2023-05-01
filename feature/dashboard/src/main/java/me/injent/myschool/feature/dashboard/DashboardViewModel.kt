package me.injent.myschool.feature.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import me.injent.myschool.core.common.util.currentLocalDate
import me.injent.myschool.core.data.repository.ScheduleRepository
import me.injent.myschool.core.data.repository.SubjectRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.repository.UserFeedRepository
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.core.model.Birthday
import me.injent.myschool.core.model.Schedule
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.model.UserFeed
import me.injent.myschool.core.ui.ScheduleUiState
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    userContextRepository: UserContextRepository,
    userFeedRepository: UserFeedRepository,
    subjectRepository: SubjectRepository,
    scheduleRepository: ScheduleRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val feedRetries = MutableStateFlow(0)
    val feedUiState: StateFlow<FeedUiState> = feedRetries.flatMapLatest {
        feedUiState(
            userFeedRepository = userFeedRepository,
            userContextRepository = userContextRepository,
            subjectRepository = subjectRepository
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeedUiState.Loading
        )

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState>
        get() = _uiState.asStateFlow()

    val scheduleUiState: StateFlow<ScheduleUiState> = networkMonitor.isOnline
        .filter { it }
        .flatMapLatest {
            scheduleUiState(
                scheduleRepository = scheduleRepository,
                uiState = _uiState
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ScheduleUiState.Loading
        )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    myName = userContextRepository.userContext.first()!!.firstName,
                    upcomingBirthdays =
                        userFeedRepository.getClosestBirthdays(LocalDate.currentLocalDate())
                )
            }
        }
    }

    fun onEvent(event: PointEvent) {
        when (event) {
            is PointEvent.GoToMarkDetails -> {
                _uiState.update { it.copy(viewingMarkDetails = event.markId) }
            }
            is PointEvent.OpenLessonDialog -> {
                _uiState.update { it.copy(viewingHomework = event.homework) }
            }
            PointEvent.CloseLessonDialog -> {
                _uiState.update { it.copy(viewingHomework = null) }
            }
            PointEvent.BackEvent -> {
                _uiState.update { it.copy(viewingMarkDetails = null) }
            }
            is PointEvent.ChangeScheduleVariant -> {
                _uiState.update { it.copy(selectedSchedule = event.variant) }
            }
            PointEvent.RetryRecentMarks -> {
                feedRetries.value++
            }
        }
    }
}

data class DashboardUiState(
    val viewingMarkDetails: Long? = null,
    val viewingHomework: Schedule.Lesson? = null,
    val myName: String = "",
    val selectedSchedule: Schedule.Variant? = null,
    val upcomingBirthdays: List<Birthday> = emptyList()
)

sealed interface FeedUiState {
    object Loading : FeedUiState
    object Error : FeedUiState
    data class Success(
        val recentMarks: List<UserFeed.RecentMark>,
        val currentLesson: UserFeed.Lesson? = null
    ) : FeedUiState
}

private fun feedUiState(
    userFeedRepository: UserFeedRepository,
    userContextRepository: UserContextRepository,
    subjectRepository: SubjectRepository
): Flow<FeedUiState> = flow {
    emit(FeedUiState.Loading)
    val userContext: UserContext = userContextRepository.userContext.first()!!
    val userFeed = userFeedRepository.getUserFeed(
        groupId = userContext.group.id,
        personId = userContext.personId,
    )
    val uiState = FeedUiState.Success(
        recentMarks = userFeed.recentMarks.map { recentMark ->
            recentMark.copy(
                subjectName = subjectRepository.getSubjectName(recentMark.subjectName.toLong())
            )
        },
        currentLesson = userFeed.currentLesson
    ) as FeedUiState
    emit(uiState)
}.catch { emit(FeedUiState.Error) }


private fun scheduleUiState(
    scheduleRepository: ScheduleRepository,
    uiState: MutableStateFlow<DashboardUiState>,
): Flow<ScheduleUiState> {
    return combine(
        scheduleRepository.getSchedule(LocalDate.currentLocalDate()),
        scheduleRepository.getSchedule(LocalDate.currentLocalDate().plus(1, DateTimeUnit.DAY))
    ) { todaySchedule, tomorrowSchedule ->
        if (tomorrowSchedule != null) {
            uiState.update {
                it.copy(selectedSchedule = Schedule.Variant.Tomorrow)
            }
        } else {
            uiState.update {
                it.copy(selectedSchedule = Schedule.Variant.Today)
            }
        }
        @Suppress("USELESS_CAST")
        ScheduleUiState.Success(
            todaySchedule = todaySchedule,
            tomorrowSchedule = tomorrowSchedule
        ) as ScheduleUiState
    }.catch { emit(ScheduleUiState.Error) }
}