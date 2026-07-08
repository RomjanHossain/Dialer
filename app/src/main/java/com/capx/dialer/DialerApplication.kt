package com.capx.dialer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DialerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
