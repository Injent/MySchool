package me.injent.myschool.feature.markpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.SubjectRepository
import me.injent.myschool.core.data.repository.UserContextRepository
import me.injent.myschool.core.data.util.NetworkMonitor
import me.injent.myschool.core.model.MarkDetails
import me.injent.myschool.core.model.Subject
import me.injent.myschool.feature.markpage.navigation.MARK_ID
import javax.inject.Inject

@HiltViewModel
class MarkDetailsViewModel @Inject constructor(
    markRepository: MarkRepository,
    userContextRepository: UserContextRepository,
    subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    private val markDetailsRetries = MutableStateFlow(0)
    val markDetailsUiState = markDetailsRetries.flatMapLatest {
        markDetailsUiState(
            markRepository = markRepository,
            userContextRepository = userContextRepository,
            subjectRepository = subjectRepository,
            markId = savedStateHandle[MARK_ID] ?: throw RuntimeException("$MARK_ID must be not null")
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MarkDetailsUiState.Loading
        )

    fun reloadData() {
        markDetailsRetries.value++
    }
}

sealed interface MarkDetailsUiState {
    object Loading : MarkDetailsUiState
    data class Success(
        val markDetails: MarkDetails,
        val subject: Subject
    ) : MarkDetailsUiState
    object Error : MarkDetailsUiState
}

private fun markDetailsUiState(
    markRepository: MarkRepository,
    userContextRepository: UserContextRepository,
    subjectRepository: SubjectRepository,
    markId: Long
): Flow<MarkDetailsUiState> {
    return combine(
        markRepository.getMark(markId),
        userContextRepository.userContext
    ) { mark, userContext ->
        if (userContext == null) return@combine MarkDetailsUiState.Error

        val subject = subjectRepository.getSubject(mark.dbSubjectId!!).first()
        val markDetails = markRepository.getMarkDetails(
            personId = userContext.personId,
            groupId = userContext.group.id,
            markId = markId
        )

        MarkDetailsUiState.Success(
            markDetails = markDetails.copy(
                categories = markDetails.categories.sortedBy { it.mood.ordinal }
            ),
            subject = subject
        )
    }
        .onStart { emit(MarkDetailsUiState.Loading) }
        .catch { emit(MarkDetailsUiState.Error); it.printStackTrace() }
}