package com.capx.dialer.feature.dialpad

import com.capx.dialer.core.domain.model.Contact

data class DialpadUiState(
    val currentNumber: String = "",
    val suggestedContacts: List<Contact> = emptyList()
)

sealed interface DialpadUiEvent {
    data class ShowSnackbar(val message: String) : DialpadUiEvent
    data object NavigateToCall : DialpadUiEvent
}
