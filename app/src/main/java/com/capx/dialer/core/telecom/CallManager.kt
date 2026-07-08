package com.capx.dialer.core.telecom

import android.content.Context
import android.media.AudioManager
import android.telecom.Call
import android.telecom.VideoProfile
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.model.DisconnectReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallManager @Inject constructor(
    private val context: Context
) {
    private val _callState = MutableStateFlow<CallState>(CallState.Idle)
    val callState: StateFlow<CallState> = _callState.asStateFlow()

    internal var activeCall: Call? = null
        private set

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            updateCallState(call)
        }

        override fun onDetailsChanged(call: Call, details: Call.Details) {
            updateCallState(call)
        }
    }

    fun onCallAdded(call: Call) {
        if (activeCall != null) {
            // We only support one call for MVP
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
            _callState.value = CallState.Disconnected(DisconnectReason.NORMAL) // Improve this by parsing call.details.disconnectCause
            
            // reset to idle after brief delay so UI can show disconnected state
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
                isMuted = audioManager.isMicrophoneMute,
                isSpeakerOn = audioManager.isSpeakerphoneOn
            )
            Call.STATE_HOLDING -> CallState.OnHold(number)
            Call.STATE_DISCONNECTED -> CallState.Disconnected()
            else -> CallState.Idle
        }
        _callState.value = state
    }

    fun endCall() {
        activeCall?.disconnect()
    }

    fun toggleMute() {
        val isMuted = !audioManager.isMicrophoneMute
        audioManager.isMicrophoneMute = isMuted
        
        val current = _callState.value
        if (current is CallState.Active) {
            _callState.value = current.copy(isMuted = isMuted)
        }
    }

    fun toggleSpeaker() {
        val isSpeaker = !audioManager.isSpeakerphoneOn
        audioManager.isSpeakerphoneOn = isSpeaker
        
        val current = _callState.value
        if (current is CallState.Active) {
            _callState.value = current.copy(isSpeakerOn = isSpeaker)
        }
    }

    fun toggleHold() {
        val call = activeCall ?: return
        if (call.state == Call.STATE_HOLDING) {
            call.unhold()
        } else if (call.state == Call.STATE_ACTIVE) {
            call.hold()
        }
    }

    fun sendDtmfTone(digit: Char) {
        activeCall?.playDtmfTone(digit)
        // Need to stop tone after a short delay or when user releases
    }

    fun answerCall() {
        activeCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    fun rejectCall() {
        activeCall?.reject(false, null)
    }
}
