package com.capx.dialer.feature.recents

import com.capx.dialer.core.domain.model.CallType

/**
 * A run of consecutive calls with the same number, collapsed into a single
 * row (like iOS / One UI). [count] shows how many calls were grouped.
 */
data class RecentCallGroup(
    val id: Long,
    val number: String,
    val name: String?,
    val photoUri: String?,
    val type: CallType,
    val timestamp: Long,
    val duration: Long,
    val count: Int
)

data class RecentsUiState(
    val groups: List<RecentCallGroup> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface RecentsUiEvent {
    data class CallNumber(val number: String) : RecentsUiEvent
    data class ShowSnackbar(val message: String) : RecentsUiEvent
}
