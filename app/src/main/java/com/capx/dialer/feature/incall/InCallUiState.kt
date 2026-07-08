package com.capx.dialer.feature.incall

import com.capx.dialer.core.domain.model.CallState

data class InCallUiState(
    val callState: CallState = CallState.Idle
)

sealed interface InCallUiEvent {
    data object Dismiss : InCallUiEvent
}
