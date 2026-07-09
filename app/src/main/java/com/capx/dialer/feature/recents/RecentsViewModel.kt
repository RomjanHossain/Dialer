package com.capx.dialer.feature.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.CallType
import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.usecase.DeleteRecentCallUseCase
import com.capx.dialer.core.domain.usecase.GetRecentCallsUseCase
import com.capx.dialer.core.ui.util.DateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
            // Group + format on a background dispatcher so the main thread stays
            // free and scrolling is smooth; rows then render pre-formatted data.
            getRecentCallsUseCase()
                .map { calls -> groupConsecutive(calls) }
                .flowOn(Dispatchers.Default)
                .collect { groups ->
                    _state.update { it.copy(groups = groups, isLoading = false) }
                }
        }
    }

    /** Collapses consecutive entries with the same number into one group. */
    private fun groupConsecutive(calls: List<RecentCall>): List<RecentCallGroup> {
        val result = ArrayList<RecentCallGroup>(calls.size)
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
                    count = j - i,
                    timeLabel = DateFormat.relative(head.timestamp),
                    subtitle = buildSubtitle(head.type, head.timestamp, head.duration)
                )
            )
            i = j
        }
        return result
    }

    private fun buildSubtitle(type: CallType, timestamp: Long, duration: Long): String {
        val label = when (type) {
            CallType.INCOMING -> "Incoming"
            CallType.OUTGOING -> "Outgoing"
            CallType.MISSED -> "Missed"
            CallType.REJECTED -> "Declined"
            CallType.BLOCKED -> "Blocked"
        }
        val time = DateFormat.relative(timestamp)
        val dur = DateFormat.duration(duration)
        return buildList {
            add(label)
            if (time.isNotBlank()) add(time)
            if (dur.isNotBlank() && (type == CallType.INCOMING || type == CallType.OUTGOING)) add(dur)
        }.joinToString(" · ")
    }

    fun onCallClick(number: String) {
        viewModelScope.launch { _events.send(RecentsUiEvent.CallNumber(number)) }
    }

    fun onDeleteCall(id: Long) {
        viewModelScope.launch { deleteRecentCallUseCase(id) }
    }
}
