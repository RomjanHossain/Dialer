package com.capx.dialer.core.domain.model

/**
 * Domain model representing a recent call log entry.
 *
 * Encapsulates all information about a single call event including
 * the associated contact info (if resolved), call direction/type,
 * timing, and optional recording reference.
 *
 * @property id Unique identifier for this call log entry.
 * @property number The phone number involved in the call.
 * @property contactName Resolved contact name, or null if the number is unknown.
 * @property contactPhotoUri URI string for the contact's photo, or null if unavailable.
 * @property type The direction/result of the call (incoming, outgoing, missed, etc.).
 * @property timestamp Unix epoch milliseconds when the call occurred.
 * @property duration Call duration in seconds (0 for missed/rejected calls).
 * @property recordingId Reference to an associated [Recording], if the call was recorded.
 */
data class RecentCall(
    val id: Long,
    val number: String,
    val contactName: String? = null,
    val contactPhotoUri: String? = null,
    val type: CallType,
    val timestamp: Long,
    val duration: Long,
    val recordingId: Long? = null
)

/**
 * Enumeration of possible call types/directions.
 *
 * Maps to the system call log types but remains decoupled
 * from Android's CallLog.Calls constants.
 */
enum class CallType {
    /** Call received and answered. */
    INCOMING,

    /** Call placed by the user. */
    OUTGOING,

    /** Incoming call that was not answered. */
    MISSED,

    /** Incoming call explicitly rejected by the user. */
    REJECTED,

    /** Call from a blocked number. */
    BLOCKED
}
