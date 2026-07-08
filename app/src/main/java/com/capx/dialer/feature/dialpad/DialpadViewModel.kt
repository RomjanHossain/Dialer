package com.capx.dialer.feature.dialpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.usecase.DialNumberUseCase
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
class DialpadViewModel @Inject constructor(
    private val dialNumberUseCase: DialNumberUseCase,
    private val searchContactsUseCase: SearchContactsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DialpadUiState())
    val state: StateFlow<DialpadUiState> = _state.asStateFlow()

    private val _events = Channel<DialpadUiEvent>()
    val events = _events.receiveAsFlow()

    fun onDigitPress(digit: Char) {
        _state.update { it.copy(currentNumber = it.currentNumber + digit) }
        searchContacts()
    }

    fun onBackspaceClick() {
        val current = _state.value.currentNumber
        if (current.isNotEmpty()) {
            _state.update { it.copy(currentNumber = current.dropLast(1)) }
            searchContacts()
        }
    }
    
    fun onBackspaceLongClick() {
        _state.update { it.copy(currentNumber = "") }
        searchContacts()
    }

    fun onCallClick() {
        val number = _state.value.currentNumber
        if (number.isNotEmpty()) {
            val result = dialNumberUseCase(number)
            if (result.isSuccess) {
                viewModelScope.launch {
                    _events.send(DialpadUiEvent.NavigateToCall)
                }
            } else {
                viewModelScope.launch {
                    _events.send(DialpadUiEvent.ShowSnackbar("Cannot place call"))
                }
            }
        }
    }

    private fun searchContacts() {
        val query = _state.value.currentNumber
        if (query.length >= 2) {
            viewModelScope.launch {
                searchContactsUseCase(query).collect { contacts ->
                    _state.update { it.copy(suggestedContacts = contacts) }
                }
            }
        } else {
            _state.update { it.copy(suggestedContacts = emptyList()) }
        }
    }
}
