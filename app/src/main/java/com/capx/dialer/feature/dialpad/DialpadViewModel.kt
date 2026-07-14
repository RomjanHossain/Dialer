package com.capx.dialer.feature.dialpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.domain.usecase.GetContactsUseCase
import com.capx.dialer.core.domain.util.T9
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

/**
 * Drives the dial-pad screen: tracks the typed number and produces contact
 * suggestions using T9 matching so the user can search by name (Arif -> 2743)
 * or by number.
 */
@HiltViewModel
class DialpadViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DialpadUiState())
    val state: StateFlow<DialpadUiState> = _state.asStateFlow()

    private val _events = Channel<DialpadUiEvent>()
    val events = _events.receiveAsFlow()

    /** In-memory contact cache used for fast T9 filtering on every keypress. */
    private var allContacts: List<Contact> = emptyList()
    private var suggestionJob: Job? = null

    init {
        viewModelScope.launch {
            getContactsUseCase().collect { contacts ->
                allContacts = contacts
                recomputeSuggestions()
            }
        }
    }

    fun onDigitPress(digit: Char) {
        _state.update { it.copy(currentNumber = it.currentNumber + digit) }
        recomputeSuggestions()
    }

    fun onBackspaceClick() {
        val current = _state.value.currentNumber
        if (current.isNotEmpty()) {
            _state.update { it.copy(currentNumber = current.dropLast(1)) }
            recomputeSuggestions()
        }
    }

    fun onBackspaceLongClick() {
        _state.update { it.copy(currentNumber = "") }
        recomputeSuggestions()
    }

    /** Places a call to whatever is currently typed. */
    fun onCallClick() {
        val number = _state.value.currentNumber
        if (number.isNotEmpty()) {
            viewModelScope.launch { _events.send(DialpadUiEvent.RequestCall(number)) }
        }
    }

    /** Places a call to a tapped suggestion. */
    fun onSuggestionClick(number: String) {
        viewModelScope.launch { _events.send(DialpadUiEvent.RequestCall(number)) }
    }

    private fun recomputeSuggestions() {
        val query = _state.value.currentNumber
        suggestionJob?.cancel()
        if (query.isBlank()) {
            _state.update { it.copy(suggestedContacts = emptyList()) }
            return
        }
        // Debounce fast typing and run T9 matching off the main thread.
        suggestionJob = viewModelScope.launch {
            delay(60)
            val matches = withContext(Dispatchers.Default) {
                allContacts.asSequence()
                    .filter { T9.matches(it, query) }
                    .take(20)
                    .toList()
            }
            _state.update { it.copy(suggestedContacts = matches) }
        }
    }
}
