package com.capx.dialer.core.domain.repository

import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.model.SimAccount
import kotlinx.coroutines.flow.StateFlow

/**
 * Bridge interface between the domain layer and the platform's telecom subsystem.
 *
 * Abstracts all telephony operations (placing calls, muting, speaker, DTMF, etc.)
 * behind a pure Kotlin interface. The concrete implementation in the data layer
 * will delegate to Android's [ConnectionService] / [InCallService] APIs.
 *
 * Exposes a reactive [callState] stream that the presentation layer observes
 * to drive the in-call UI through the [CallState] sealed hierarchy.
 *
 * This interface lives in the domain layer and must have ZERO Android dependencies.
 */
interface TelecomBridge {

    /**
     * Returns the list of call-capable SIM / phone accounts on the device.
     *
     * Used to decide whether a SIM chooser is needed and to place a call on a
     * specific SIM. Returns an empty list if the accounts cannot be read
     * (e.g. missing permission).
     */
    fun getSimAccounts(): List<SimAccount>

    /**
     * Initiates an outgoing call to the given phone [number].
     *
     * @param number The phone number to dial. Must be a valid, non-empty string.
     * @param accountId Optional [SimAccount.id] identifying which SIM to use.
     *        When null, the platform default is used.
     */
    fun placeCall(number: String, accountId: String? = null)

    /**
     * Ends the currently active or ringing call.
     */
    fun endCall()

    /**
     * Toggles the microphone mute state for the active call.
     *
     * If currently muted, the mic will be unmuted, and vice versa.
     */
    fun toggleMute()

    /**
     * Toggles the speakerphone for the active call.
     *
     * If the speaker is currently on, it will be turned off, and vice versa.
     */
    fun toggleSpeaker()

    /**
     * Toggles the hold state for the active call.
     *
     * If the call is active, it will be placed on hold.
     * If the call is on hold, it will be resumed.
     */
    fun toggleHold()

    /**
     * Starts or stops recording the active call. Reflected in
     * [com.capx.dialer.core.domain.model.CallState.Active.isRecording].
     */
    fun toggleRecording()

    /**
     * Sends a DTMF (Dual-Tone Multi-Frequency) tone for the given [digit].
     *
     * Valid digits are '0'-'9', '*', and '#'.
     *
     * @param digit The DTMF digit to send.
     */
    fun sendDtmfTone(digit: Char)

    /**
     * Answers an incoming ringing call.
     */
    fun answerCall()

    /**
     * Rejects an incoming ringing call without answering.
     */
    fun rejectCall()

    /**
     * Reactive stream of the current [CallState].
     *
     * Emits the latest call state whenever a transition occurs.
     * Starts with [CallState.Idle] when no call is active.
     */
    val callState: StateFlow<CallState>
}
