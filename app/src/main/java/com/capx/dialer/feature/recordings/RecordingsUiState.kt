package com.capx.dialer.feature.recordings

import com.capx.dialer.core.domain.model.Recording

data class RecordingsUiState(
    val recordings: List<Recording> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface RecordingsUiEvent {
    data class ShowSnackbar(val message: String) : RecordingsUiEvent
}
