package com.capx.dialer.feature.recordings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.usecase.GetRecordingsUseCase
import com.capx.dialer.core.domain.usecase.DeleteRecordingUseCase
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
class RecordingsViewModel @Inject constructor(
    private val getRecordingsUseCase: GetRecordingsUseCase,
    private val deleteRecordingUseCase: DeleteRecordingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecordingsUiState())
    val state: StateFlow<RecordingsUiState> = _state.asStateFlow()

    private val _events = Channel<RecordingsUiEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadRecordings()
    }

    private fun loadRecordings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getRecordingsUseCase().collect { recordings ->
                _state.update { it.copy(recordings = recordings, isLoading = false) }
            }
        }
    }

    fun onDeleteRecording(id: Long) {
        viewModelScope.launch {
            deleteRecordingUseCase(id)
        }
    }
}
