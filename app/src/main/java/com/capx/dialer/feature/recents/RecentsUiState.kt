package com.capx.dialer.feature.recents

import com.capx.dialer.core.domain.model.RecentCall

data class RecentsUiState(
    val calls: List<RecentCall> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface RecentsUiEvent {
    data class CallNumber(val number: String) : RecentsUiEvent
    data class ShowSnackbar(val message: String) : RecentsUiEvent
}
