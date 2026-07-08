package com.capx.dialer.feature.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.usecase.GetContactsUseCase
import com.capx.dialer.core.domain.usecase.GetFavoritesUseCase
import com.capx.dialer.core.domain.usecase.SearchContactsUseCase
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
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val searchContactsUseCase: SearchContactsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsUiState())
    val state: StateFlow<ContactsUiState> = _state.asStateFlow()

    private val _events = Channel<ContactsUiEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadContacts()
        loadFavorites()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getContactsUseCase().collect { contacts ->
                _state.update { it.copy(contacts = contacts, isLoading = false) }
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _state.update { it.copy(favorites = favorites) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadContacts()
            } else {
                searchContactsUseCase(query).collect { results ->
                    _state.update { it.copy(contacts = results) }
                }
            }
        }
    }

    fun onContactClick(contactId: Long) {
        viewModelScope.launch {
            _events.send(ContactsUiEvent.NavigateToContactDetail(contactId))
        }
    }

    fun onCallClick(number: String) {
        viewModelScope.launch {
            _events.send(ContactsUiEvent.CallContact(number))
        }
    }
}
