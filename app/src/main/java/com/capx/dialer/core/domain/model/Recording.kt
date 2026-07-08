package com.capx.dialer.core.domain.model

/**
 * Domain model representing a call recording.
 *
 * Stores metadata about a recorded phone call including file location,
 * duration, and optional association with a specific call log entry.
 *
 * @property id Unique identifier for the recording (auto-generated when persisted).
 * @property callId Optional reference to the associated [RecentCall.id].
 * @property filePath Absolute path to the recording file on disk.
 * @property duration Recording duration in seconds.
 * @property timestamp Unix epoch milliseconds when the recording was created.
 * @property contactName Name of the contact in the recorded call, if resolved.
 * @property contactNumber Phone number of the other party in the recorded call.
 * @property fileSize Size of the recording file in bytes.
 * @property isEncrypted Whether the recording file is encrypted at rest.
 */
data class Recording(
    val id: Long = 0,
    val callId: Long? = null,
    val filePath: String,
    val duration: Long,
    val timestamp: Long,
    val contactName: String? = null,
    val contactNumber: String? = null,
    val fileSize: Long = 0,
    val isEncrypted: Boolean = false
)
