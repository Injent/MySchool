package me.injent.myschool.core.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Implementation of basic things used in ViewModels
 * Warning: logic in this class must't be edited!
 */
abstract class BaseViewModel<STATE, EVENT, ACTION> : ViewModel() {

    /**
     * Used for receive [STATE] in UI
     */
    abstract val uiState: StateFlow<STATE>

    private val uiEventChannel = Channel<EVENT?>(BUFFERED)

    /**
     * Used in UI to collect events from ViewModel
     */
    val uiEvents = uiEventChannel.receiveAsFlow()

    private val _actions = Channel<ACTION>(BUFFERED)

    init {
        viewModelScope.launch {
            _actions.consumeEach { action ->
                processAction(action)
            }
        }
    }

    /**
     * Used for handling actions from UI
     * Fires when new event received
     */
    protected abstract fun processAction(action: ACTION)

    /**
     * Used for sending events from ViewModel to UI
     */
    protected fun sendEvent(event: EVENT) {
        viewModelScope.launch {
            uiEventChannel.send(null)
            uiEventChannel.send(event)
        }
    }

    /**
     * Used for sending actions from UI to ViewModel
     */
    fun applyAction(action: ACTION) {
        viewModelScope.launch {
            _actions.send(action)
        }
    }
}