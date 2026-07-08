package com.capx.dialer.feature.contacts

import com.capx.dialer.core.domain.model.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val favorites: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

sealed interface ContactsUiEvent {
    data class NavigateToContactDetail(val contactId: Long) : ContactsUiEvent
    data class CallContact(val number: String) : ContactsUiEvent
}
