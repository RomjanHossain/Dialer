package com.capx.dialer.feature.calllog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.usecase.GetRecentCallsByNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogDetailViewModel @Inject constructor(
    private val getRecentCallsByNumberUseCase: GetRecentCallsByNumberUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val number: String = savedStateHandle.get<String>("number").orEmpty()

    private val _state = MutableStateFlow(CallLogDetailUiState(number = number))
    val state: StateFlow<CallLogDetailUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getRecentCallsByNumberUseCase(number).collect { calls ->
                val head = calls.firstOrNull()
                _state.update {
                    it.copy(
                        calls = calls,
                        name = head?.contactName,
                        photoUri = head?.contactPhotoUri,
                        isLoading = false
                    )
                }
            }
        }
    }
}
