package com.capx.dialer.core.telecom

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.model.DisconnectReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central holder for the active call and its controls. The [DialerInCallService]
 * attaches itself here so audio operations (mute / speaker) go through the
 * proper telecom APIs rather than [android.media.AudioManager], and audio-state
 * callbacks flow back to update the UI's active/inactive indicators.
 *
 * Instantiated as a singleton by [com.capx.dialer.di.TelecomModule] (which
 * supplies the application [Context]); do not add an `@Inject` constructor here
 * or it would duplicate that binding.
 */
class CallManager(
    private val context: Context
) {
    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    internal var activeCall: Call? = null
        private set

    /** Set by [DialerInCallService] while it is bound. */
    private var inCallService: InCallService? = null

    // Control state — the source of truth for the UI's toggle indicators.
    private var isMuted = false
    private var isSpeakerOn = false
    private var isRecording = false

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) = updateCallState(call)
        override fun onDetailsChanged(call: Call, details: Call.Details) = updateCallState(call)
    }

    // ── Service lifecycle ─────────────────────────────────────────────────

    fun attachService(service: InCallService) {
        inCallService = service
    }

    fun detachService(service: InCallService) {
        if (inCallService === service) inCallService = null
    }

    /** Called by the service when the platform audio state changes. */
    fun onAudioStateChanged(state: CallAudioState) {
        isMuted = state.isMuted
        isSpeakerOn = state.route == CallAudioState.ROUTE_SPEAKER
        refreshActive()
    }

    // ── Call lifecycle ────────────────────────────────────────────────────

    fun onCallAdded(call: Call) {
        if (activeCall != null) {
            call.reject(false, null)
            return
        }
        activeCall = call
        call.registerCallback(callCallback)
        updateCallState(call)
    }

    fun onCallRemoved(call: Call) {
        if (activeCall == call) {
            call.unregisterCallback(callCallback)
            activeCall = null
            if (isRecording) stopRecording()
            isMuted = false
            isSpeakerOn = false
            _callState.value = CallState.Disconnected(DisconnectReason.NORMAL)
            _callState.value = CallState.Idle
        }
    }

    private fun updateCallState(call: Call) {
        val number = call.details?.handle?.schemeSpecificPart ?: "Unknown"
        val state = when (call.state) {
            Call.STATE_CONNECTING, Call.STATE_DIALING -> CallState.Dialing(number)
            Call.STATE_RINGING -> CallState.Ringing(number)
            Call.STATE_ACTIVE -> CallState.Active(
                number = number,
                isMuted = isMuted,
                isSpeakerOn = isSpeakerOn,
                isRecording = isRecording
            )
            Call.STATE_HOLDING -> CallState.OnHold(number)
            Call.STATE_DISCONNECTED -> CallState.Disconnected()
            else -> CallState.Idle
        }
        _callState.value = state
    }

    /** Re-emits the current Active state with refreshed control flags. */
    private fun refreshActive() {
        val current = _callState.value
        if (current is CallState.Active) {
            _callState.value = current.copy(
                isMuted = isMuted,
                isSpeakerOn = isSpeakerOn,
                isRecording = isRecording
            )
        }
    }

    // ── Controls ──────────────────────────────────────────────────────────

    fun endCall() {
        activeCall?.disconnect()
    }

    fun toggleMute() {
        val target = !isMuted
        inCallService?.setMuted(target)
        // Optimistic update in case the audio-state callback is delayed.
        isMuted = target
        refreshActive()
    }

    fun toggleSpeaker() {
        val target = !isSpeakerOn
        inCallService?.setAudioRoute(
            if (target) CallAudioState.ROUTE_SPEAKER else CallAudioState.ROUTE_EARPIECE
        )
        isSpeakerOn = target
        refreshActive()
    }

    fun toggleRecording() {
        if (isRecording) stopRecording() else startRecording()
    }

    private fun startRecording() {
        val intent = Intent(context, CallRecordingService::class.java).apply {
            action = CallRecordingService.ACTION_START
        }
        val started = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }.isSuccess
        isRecording = started
        refreshActive()
    }

    private fun stopRecording() {
        val intent = Intent(context, CallRecordingService::class.java).apply {
            action = CallRecordingService.ACTION_STOP
        }
        runCatching { context.startService(intent) }
        isRecording = false
        refreshActive()
    }

    fun toggleHold() {
        val call = activeCall ?: return
        when (call.state) {
            Call.STATE_HOLDING -> call.unhold()
            Call.STATE_ACTIVE -> call.hold()
        }
    }

    fun sendDtmfTone(digit: Char) {
        activeCall?.playDtmfTone(digit)
        activeCall?.stopDtmfTone()
    }

    fun answerCall() {
        activeCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun rejectCall() {
        activeCall?.reject(false, null)
    }
}
