package com.capx.dialer.core.telecom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.EntryPointAccessors

/**
 * Handles call-control actions triggered from the call notification
 * (answer / decline / end). Uses a Hilt [EntryPoint] to reach the singleton
 * [CallManager] since [BroadcastReceiver]s cannot use constructor injection.
 */
class CallActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val callManager = EntryPointAccessors
            .fromApplication(
                context.applicationContext,
                DialerInCallService.DialerInCallServiceEntryPoint::class.java
            )
            .callManager()

        when (intent.action) {
            ACTION_ANSWER -> callManager.answerCall()
            ACTION_DECLINE -> callManager.rejectCall()
            ACTION_END -> callManager.endCall()
        }
    }

    companion object {
        const val ACTION_ANSWER = "com.capx.dialer.action.ANSWER"
        const val ACTION_DECLINE = "com.capx.dialer.action.DECLINE"
        const val ACTION_END = "com.capx.dialer.action.END"
    }
}
