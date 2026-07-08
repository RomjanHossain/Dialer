package com.capx.dialer.core.domain.model

/**
 * Sealed class representing the current state of an active or recent call.
 *
 * Used as the primary state type for the in-call UI, emitted by
 * [com.capx.dialer.core.domain.repository.TelecomBridge] as a [StateFlow].
 * Each subclass carries the relevant data for that particular call phase.
 *
 * State transitions follow this typical flow:
 * ```
 * Idle → Dialing → Active → Disconnected → Idle
 * Idle → Ringing → Active → OnHold → Active → Disconnected → Idle
 * ```
 */
sealed class CallState {

    /** No active call. The default/rest state. */
    data object Idle : CallState()

    /**
     * An outgoing call is being placed but has not yet been answered.
     *
     * @property number The dialed phone number.
     * @property contactName Resolved contact name, if available.
     */
    data class Dialing(
        val number: String,
        val contactName: String? = null
    ) : CallState()

    /**
     * An incoming call is ringing and awaiting user action.
     *
     * @property number The caller's phone number.
     * @property contactName Resolved contact name, if available.
     */
    data class Ringing(
        val number: String,
        val contactName: String? = null
    ) : CallState()

    /**
     * A call is currently connected and active.
     *
     * @property number The remote party's phone number.
     * @property contactName Resolved contact name, if available.
     * @property contactPhotoUri URI string for the contact's photo.
     * @property startTime Unix epoch millis when the call became active.
     * @property isRecording Whether call recording is currently active.
     * @property isMuted Whether the microphone is muted.
     * @property isSpeakerOn Whether the speakerphone is engaged.
     * @property isOnHold Whether the call is currently on hold.
     */
    data class Active(
        val number: String,
        val contactName: String? = null,
        val contactPhotoUri: String? = null,
        val startTime: Long = System.currentTimeMillis(),
        val isRecording: Boolean = false,
        val isMuted: Boolean = false,
        val isSpeakerOn: Boolean = false,
        val isOnHold: Boolean = false
    ) : CallState()

    /**
     * The call has been placed on hold by the user.
     *
     * @property number The remote party's phone number.
     * @property contactName Resolved contact name, if available.
     */
    data class OnHold(
        val number: String,
        val contactName: String? = null
    ) : CallState()

    /**
     * The call has ended. Carries a [reason] for the disconnection.
     *
     * @property reason Why the call was disconnected.
     */
    data class Disconnected(
        val reason: DisconnectReason = DisconnectReason.NORMAL
    ) : CallState()
}

/**
 * Enumeration of reasons a call may be disconnected.
 */
enum class DisconnectReason {
    /** Normal hangup by either party. */
    NORMAL,

    /** Remote party's line was busy. */
    BUSY,

    /** Call was explicitly rejected. */
    REJECTED,

    /** Incoming call was not answered (timed out). */
    MISSED,

    /** A network or system error terminated the call. */
    ERROR,

    /** Disconnect reason could not be determined. */
    UNKNOWN
}
