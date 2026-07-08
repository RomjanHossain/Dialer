package com.capx.dialer.core.telecom

import android.content.Intent
import android.telecom.Call
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

    override fun onCreate() {
        super.onCreate()
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            DialerInCallServiceEntryPoint::class.java
        )
        callManager = entryPoint.callManager()
    }

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        callManager.onCallAdded(call)
        launchInCallUi()
    }

    /** Brings up the app's own full-screen calling UI. */
    private fun launchInCallUi() {
        val intent = Intent(this, InCallActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        callManager.onCallRemoved(call)
    }
}
