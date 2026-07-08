package com.capx.dialer.core.telecom

import android.telecom.Call
import android.telecom.InCallService
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
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        callManager.onCallRemoved(call)
    }
}
