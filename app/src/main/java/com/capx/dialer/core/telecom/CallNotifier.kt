package com.capx.dialer.core.telecom

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.Call
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.capx.dialer.feature.incall.InCallActivity

/**
 * Posts and updates the notification for the active call.
 *
 * Tapping the notification opens the app's own [InCallActivity]; incoming
 * calls use a full-screen intent so the calling screen appears even from the
 * lock screen, and expose Answer / Decline actions. Ongoing calls expose End.
 */
class CallNotifier(private val context: Context) {

    private val manager = NotificationManagerCompat.from(context)

    init {
        createChannels()
    }

    fun showForCall(call: Call) {
        when (call.state) {
            Call.STATE_RINGING -> notifyIncoming(call)
            Call.STATE_DISCONNECTED, Call.STATE_DISCONNECTING -> cancel()
            else -> notifyOngoing(call)
        }
    }

    fun cancel() {
        manager.cancel(NOTIFICATION_ID)
    }

    // ── Builders ──────────────────────────────────────────────────────────

    private fun notifyIncoming(call: Call) {
        val caller = callerLabel(call)
        val builder = NotificationCompat.Builder(context, CHANNEL_INCOMING)
            .setContentTitle("Incoming call")
            .setContentText(caller)
            .setSmallIcon(android.R.drawable.sym_call_incoming)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setFullScreenIntent(contentIntent(), true)
            .setContentIntent(contentIntent())
            .addAction(
                android.R.drawable.sym_action_call,
                "Answer",
                actionIntent(CallActionReceiver.ACTION_ANSWER, 1)
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Decline",
                actionIntent(CallActionReceiver.ACTION_DECLINE, 2)
            )
        post(builder)
    }

    private fun notifyOngoing(call: Call) {
        val caller = callerLabel(call)
        val status = if (call.state == Call.STATE_DIALING || call.state == Call.STATE_CONNECTING) {
            "Calling…"
        } else {
            "Ongoing call"
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ONGOING)
            .setContentTitle(caller)
            .setContentText(status)
            .setSmallIcon(android.R.drawable.sym_action_call)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setContentIntent(contentIntent())
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "End",
                actionIntent(CallActionReceiver.ACTION_END, 3)
            )
        post(builder)
    }

    private fun post(builder: NotificationCompat.Builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        runCatching { manager.notify(NOTIFICATION_ID, builder.build()) }
    }

    private fun callerLabel(call: Call): String {
        val number = call.details?.handle?.schemeSpecificPart
        return number?.takeIf { it.isNotBlank() } ?: "Unknown"
    }

    private fun contentIntent(): PendingIntent {
        val intent = Intent(context, InCallActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun actionIntent(action: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, CallActionReceiver::class.java).setAction(action)
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_INCOMING,
                "Incoming calls",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        nm.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ONGOING,
                "Ongoing calls",
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    companion object {
        private const val CHANNEL_INCOMING = "incoming_call_channel"
        private const val CHANNEL_ONGOING = "ongoing_call_channel"
        private const val NOTIFICATION_ID = 42
    }
}
