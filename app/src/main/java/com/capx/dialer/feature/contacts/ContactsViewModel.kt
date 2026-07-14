package com.capx.dialer.feature.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.domain.usecase.GetContactsUseCase
import com.capx.dialer.core.domain.usecase.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ContactsUiState())
    val state: StateFlow<ContactsUiState> = _state.asStateFlow()

    private val _events = Channel<ContactsUiEvent>()
    val events = _events.receiveAsFlow()

    /** Full contact list kept in memory so search filters instantly. */
    private var allContacts: List<Contact> = emptyList()
    private var searchJob: Job? = null

    init {
        observeContacts()
        loadFavorites()
    }

    private fun observeContacts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getContactsUseCase().collect { contacts ->
                allContacts = contacts
                applyFilter(_state.value.searchQuery)
                _state.update { it.copy(isLoading = false) }
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
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(120) // debounce keystrokes
            applyFilter(query)
        }
    }

    /** Filters the in-memory list locally (no per-keystroke provider query). */
    private suspend fun applyFilter(query: String) {
        val q = query.trim()
        val filtered = if (q.isEmpty()) {
            allContacts
        } else {
            withContext(Dispatchers.Default) {
                val lower = q.lowercase()
                val digits = q.filter { it.isDigit() }
                allContacts.filter { contact ->
                    contact.name.lowercase().contains(lower) ||
                        (digits.isNotEmpty() &&
                            contact.phoneNumber.filter { it.isDigit() }.contains(digits))
                }
            }
        }
        _state.update { it.copy(contacts = filtered) }
    }

    fun onContactClick(contactId: Long) {
        viewModelScope.launch { _events.send(ContactsUiEvent.NavigateToContactDetail(contactId)) }
    }

    fun onCallClick(number: String) {
        viewModelScope.launch { _events.send(ContactsUiEvent.CallContact(number)) }
    }
}
