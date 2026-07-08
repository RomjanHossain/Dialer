package com.capx.dialer.feature.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.usecase.GetRecentCallsUseCase
import com.capx.dialer.core.domain.usecase.DeleteRecentCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val getRecentCallsUseCase: GetRecentCallsUseCase,
    private val deleteRecentCallUseCase: DeleteRecentCallUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecentsUiState())
    val state: StateFlow<RecentsUiState> = _state.asStateFlow()

    private val _events = Channel<RecentsUiEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadRecents()
    }

    private fun loadRecents() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getRecentCallsUseCase().collect { calls ->
                _state.update { it.copy(calls = calls, isLoading = false) }
            }
        }
    }

    fun onCallClick(number: String) {
        viewModelScope.launch {
            _events.send(RecentsUiEvent.CallNumber(number))
        }
    }

    fun onDeleteCall(id: Long) {
        viewModelScope.launch {
            deleteRecentCallUseCase(id)
        }
    }
}
