package com.capx.dialer.feature.incall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.repository.TelecomBridge
import com.capx.dialer.core.domain.usecase.ObserveActiveCallUseCase
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
class InCallViewModel @Inject constructor(
    private val observeActiveCallUseCase: ObserveActiveCallUseCase,
    private val telecomBridge: TelecomBridge
) : ViewModel() {

    private val _state = MutableStateFlow(InCallUiState())
    val state: StateFlow<InCallUiState> = _state.asStateFlow()

    private val _events = Channel<InCallUiEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            observeActiveCallUseCase().collect { callState ->
                _state.update { it.copy(callState = callState) }
                // Dismiss the in-call screen once the call is over.
                if (callState is CallState.Idle || callState is CallState.Disconnected) {
                    _events.send(InCallUiEvent.Dismiss)
                }
            }
        }
    }

    fun answerCall() = telecomBridge.answerCall()

    fun rejectCall() = telecomBridge.rejectCall()

    fun toggleMute() = telecomBridge.toggleMute()

    fun toggleSpeaker() = telecomBridge.toggleSpeaker()

    fun toggleHold() = telecomBridge.toggleHold()

    fun toggleRecording() = telecomBridge.toggleRecording()

    fun endCall() = telecomBridge.endCall()

    fun sendDtmf(digit: Char) = telecomBridge.sendDtmfTone(digit)
}
