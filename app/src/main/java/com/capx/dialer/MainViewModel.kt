package com.capx.dialer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capx.dialer.core.domain.model.SimAccount
import com.capx.dialer.core.domain.usecase.DialNumberUseCase
import com.capx.dialer.core.domain.usecase.GetSimAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * App-level view model shared by every screen. It owns the single source of
 * truth for placing calls so that Dialpad, Recents and Contacts all funnel
 * through the same fast SIM-selection flow instead of the slow OS chooser.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSimAccountsUseCase: GetSimAccountsUseCase,
    private val dialNumberUseCase: DialNumberUseCase
) : ViewModel() {

    data class UiState(
        val sims: List<SimAccount> = emptyList(),
        /** Number awaiting a SIM choice; non-null shows the SIM picker sheet. */
        val pendingCallNumber: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    /** Load SIM accounts up-front so the picker can appear instantly. */
    fun refreshSims() {
        viewModelScope.launch(Dispatchers.IO) {
            val sims = getSimAccountsUseCase()
            _state.update { it.copy(sims = sims) }
        }
    }

    /**
     * Entry point for every "call this number" action in the app.
     * Places the call directly when there is zero or one SIM; otherwise it
     * shows the in-app SIM picker.
     */
    fun requestCall(number: String) {
        if (number.isBlank()) return
        val sims = _state.value.sims
        if (sims.size <= 1) {
            dialNumberUseCase(number, sims.firstOrNull()?.id)
        } else {
            _state.update { it.copy(pendingCallNumber = number) }
        }
    }

    fun onSimChosen(sim: SimAccount) {
        val number = _state.value.pendingCallNumber ?: return
        dialNumberUseCase(number, sim.id)
        _state.update { it.copy(pendingCallNumber = null) }
    }

    fun dismissSimPicker() {
        _state.update { it.copy(pendingCallNumber = null) }
    }
}
