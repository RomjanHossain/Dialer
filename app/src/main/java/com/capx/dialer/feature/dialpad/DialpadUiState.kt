package com.capx.dialer.feature.dialpad

import com.capx.dialer.core.domain.model.Contact

data class DialpadUiState(
    val currentNumber: String = "",
    val suggestedContacts: List<Contact> = emptyList()
)

sealed interface DialpadUiEvent {
    data class ShowSnackbar(val message: String) : DialpadUiEvent

    /** Ask the host to place a call to [number] (routed through SIM selection). */
    data class RequestCall(val number: String) : DialpadUiEvent
}
