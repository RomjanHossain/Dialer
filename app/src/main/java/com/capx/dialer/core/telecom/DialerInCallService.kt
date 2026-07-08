package com.capx.dialer.core.telecom

import android.content.Intent
import android.telecom.Call
import android.telecom.CallAudioState
import android.telecom.InCallService
import com.capx.dialer.feature.incall.InCallActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class DialerInCallService : InCallService() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface DialerInCallServiceEntryPoint {
        fun callManager(): CallManager
    }

    private lateinit var callManager: CallManager
    private lateinit var callNotifier: CallNotifier

    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            DialerInCallServiceEntryPoint::class.java
        )
        callManager = entryPoint.callManager()
        callManager.attachService(this)
        callNotifier = CallNotifier(this)
    }

    override fun onDestroy() {
        callManager.detachService(this)
        super.onDestroy()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        callManager.onCallAdded(call)
        launchInCallUi()
        callNotifier.showForCall(call)
        call.registerCallback(notificationCallback)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        call.unregisterCallback(notificationCallback)
        callManager.onCallRemoved(call)
        callNotifier.cancel()
    }

    override fun onCallAudioStateChanged(audioState: CallAudioState) {
        super.onCallAudioStateChanged(audioState)
        callManager.onAudioStateChanged(audioState)
    }

    /** Keeps the ongoing-call notification in sync with call state changes. */
    private val notificationCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            callNotifier.showForCall(call)
        }
    }

    /** Brings up the app's own full-screen calling UI. */
    private fun launchInCallUi() {
        val intent = Intent(this, InCallActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
