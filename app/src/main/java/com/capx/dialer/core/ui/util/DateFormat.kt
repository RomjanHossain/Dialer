package com.capx.dialer.core.ui.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Lightweight, allocation-friendly date/duration formatting for the call log.
 * Pure Kotlin/JVM — no Android dependencies, so it stays testable.
 */
object DateFormat {

    private val timeFmt = SimpleDateFormat("h:mm a", Locale.getDefault())
    private val weekdayFmt = SimpleDateFormat("EEEE", Locale.getDefault())
    private val dateFmt = SimpleDateFormat("d MMM", Locale.getDefault())
    private val dateYearFmt = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    /**
     * Human-friendly relative label for a call timestamp:
     * "9:41 AM" today, "Yesterday", a weekday name within the last week,
     * "12 Jun" within the year, or "12 Jun 2024" beyond that.
     */
    fun relative(timestampMillis: Long, now: Long = System.currentTimeMillis()): String {
        if (timestampMillis <= 0L) return ""
        val then = Calendar.getInstance().apply { timeInMillis = timestampMillis }
        val today = Calendar.getInstance().apply { timeInMillis = now }

        fun sameDay(offsetDays: Int): Boolean {
            val ref = Calendar.getInstance().apply {
                timeInMillis = now
                add(Calendar.DAY_OF_YEAR, -offsetDays)
            }
            return then.get(Calendar.YEAR) == ref.get(Calendar.YEAR) &&
                then.get(Calendar.DAY_OF_YEAR) == ref.get(Calendar.DAY_OF_YEAR)
        }

        // SimpleDateFormat is not thread-safe and this runs on multiple
        // dispatchers (recents grouping + detail sheet), so guard formatting.
        val date = Date(timestampMillis)
        return synchronized(this) {
            when {
                sameDay(0) -> timeFmt.format(date)
                sameDay(1) -> "Yesterday"
                (now - timestampMillis) < 7L * 24 * 60 * 60 * 1000 -> weekdayFmt.format(date)
                then.get(Calendar.YEAR) == today.get(Calendar.YEAR) -> dateFmt.format(date)
                else -> dateYearFmt.format(date)
            }
        }
    }

    /** Formats a call duration in seconds as "m:ss" or "h:mm:ss". */
    fun duration(seconds: Long): String {
        if (seconds <= 0L) return ""
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) {
            String.format(Locale.getDefault(), "%d:%02d:%02d", h, m, s)
        } else {
            String.format(Locale.getDefault(), "%d:%02d", m, s)
        }
    }
}
