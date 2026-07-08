package com.capx.dialer.core.telecom

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.model.SimAccount
import com.capx.dialer.core.domain.repository.TelecomBridge
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TelecomBridgeImpl @Inject constructor(
    private val context: Context,
    private val callManager: CallManager
) : TelecomBridge {

    private val telecomManager =
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    /**
     * Cache mapping our opaque [SimAccount.id] back to the real platform
     * [PhoneAccountHandle]. Populated by [getSimAccounts] and read by
     * [placeCall]. Keeping the real handle avoids reconstructing it (which
     * needs the owning ComponentName) and lets us place calls instantly on a
     * chosen SIM without the slow system SIM chooser.
     */
    private val handleCache = mutableMapOf<String, PhoneAccountHandle>()

    override fun getSimAccounts(): List<SimAccount> {
        return try {
            val handles = telecomManager.callCapablePhoneAccounts
            handleCache.clear()
            handles.mapIndexedNotNull { index, handle ->
                val account = runCatching { telecomManager.getPhoneAccount(handle) }.getOrNull()
                val id = handle.id
                if (id.isNullOrBlank()) return@mapIndexedNotNull null
                handleCache[id] = handle
                SimAccount(
                    id = id,
                    label = account?.label?.toString()?.takeIf { it.isNotBlank() }
                        ?: "SIM ${index + 1}",
                    slotIndex = index
                )
            }
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    override fun placeCall(number: String, accountId: String?) {
        val uri = Uri.fromParts("tel", number, null)
        val extras = Bundle()

        // Resolve the chosen SIM handle; refresh the cache if it's empty.
        val handle = accountId?.let { id ->
            handleCache[id] ?: run {
                getSimAccounts()
                handleCache[id]
            }
        }
        if (handle != null) {
            extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, handle)
        }

        try {
            telecomManager.placeCall(uri, extras)
        } catch (e: SecurityException) {
            // Missing CALL_PHONE permission — surfaced to the user upstream.
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
