package com.capx.dialer.core.telecom

import android.content.Context
import android.net.Uri
import android.telecom.TelecomManager
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.repository.TelecomBridge
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TelecomBridgeImpl @Inject constructor(
    private val context: Context,
    private val callManager: CallManager
) : TelecomBridge {

    private val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    override fun placeCall(number: String) {
        val uri = Uri.fromParts("tel", number, null)
        try {
            telecomManager.placeCall(uri, null)
        } catch (e: SecurityException) {
            // Handle missing permissions
        }
    }

    override fun endCall() {
        callManager.endCall()
    }

    override fun toggleMute() {
        callManager.toggleMute()
    }

    override fun toggleSpeaker() {
        callManager.toggleSpeaker()
    }

    override fun toggleHold() {
        callManager.toggleHold()
    }

    override fun sendDtmfTone(digit: Char) {
        callManager.sendDtmfTone(digit)
    }

    override fun answerCall() {
        callManager.answerCall()
    }

    override fun rejectCall() {
        callManager.rejectCall()
    }

    override val callState: StateFlow<CallState>
        get() = callManager.callState
}
