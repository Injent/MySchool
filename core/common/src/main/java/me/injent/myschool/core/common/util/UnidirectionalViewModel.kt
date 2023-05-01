package me.injent.myschool.core.common.util

import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<STATE, EVENT> {
    val state: StateFlow<STATE>
    fun onEvent(event: EVENT)
}