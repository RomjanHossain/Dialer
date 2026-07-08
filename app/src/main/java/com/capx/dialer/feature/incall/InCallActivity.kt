package com.capx.dialer.feature.incall

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.capx.dialer.core.ui.theme.DialerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Full-screen Activity that hosts the app's own in-call UI. It is launched by
 * [com.capx.dialer.core.telecom.DialerInCallService] whenever a call is added,
 * which is what makes the custom calling screen appear instead of the system
 * one. Shows over the lock screen and turns the display on for incoming calls.
 */
@AndroidEntryPoint
class InCallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWhenLockedAndTurnScreenOn()
        enableEdgeToEdge()
        setContent {
            DialerTheme {
                InCallScreen(onDismiss = { finish() })
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }
}
