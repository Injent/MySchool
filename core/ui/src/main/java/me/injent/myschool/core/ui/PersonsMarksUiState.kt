package me.injent.myschool.core.ui

import me.injent.myschool.core.model.PersonAndMarkValue

sealed interface PersonsMarksUiState {
    object Loading : PersonsMarksUiState
    data class Success(
        val persons: List<PersonAndMarkValue>,
    ) : PersonsMarksUiState
}