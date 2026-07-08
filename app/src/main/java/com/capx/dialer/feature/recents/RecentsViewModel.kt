package com.capx.dialer.feature.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.usecase.DeleteRecentCallUseCase
import com.capx.dialer.core.domain.usecase.GetRecentCallsUseCase
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
                _state.update { it.copy(groups = groupConsecutive(calls), isLoading = false) }
            }
        }
    }

    /** Collapses consecutive entries with the same number into one group. */
    private fun groupConsecutive(calls: List<RecentCall>): List<RecentCallGroup> {
        val result = mutableListOf<RecentCallGroup>()
        var i = 0
        while (i < calls.size) {
            val head = calls[i]
            var j = i + 1
            while (j < calls.size && calls[j].number == head.number && calls[j].number.isNotBlank()) {
                j++
            }
            result.add(
                RecentCallGroup(
                    id = head.id,
                    number = head.number,
                    name = head.contactName,
                    photoUri = head.contactPhotoUri,
                    type = head.type,
                    timestamp = head.timestamp,
                    duration = head.duration,
                    count = j - i
                )
            )
            i = j
        }
        return result
    }

    fun onCallClick(number: String) {
        viewModelScope.launch { _events.send(RecentsUiEvent.CallNumber(number)) }
    }

    fun onDeleteCall(id: Long) {
        viewModelScope.launch { deleteRecentCallUseCase(id) }
    }
}
