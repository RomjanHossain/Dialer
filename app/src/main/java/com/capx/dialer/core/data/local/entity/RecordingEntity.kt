package com.capx.dialer.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a call recording stored locally.
 *
 * Each recording is associated with a specific call via [callId] and
 * stores the file system path, duration, size, and creation timestamp.
 *
 * @property id Auto-generated unique identifier.
 * @property callId Identifier of the call this recording belongs to.
 * @property filePath Absolute path to the recording file on disk.
 * @property durationMs Duration of the recording in milliseconds.
 * @property sizeBytes File size in bytes.
 * @property createdAt Epoch timestamp (millis) when the recording was created.
 * @property phoneNumber The phone number associated with the recorded call.
 * @property contactName Optional display name resolved from contacts at record time.
 */
@Entity(tableName = "recordings")
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val callId: Long,
    val filePath: String,
    val durationMs: Long,
    val sizeBytes: Long,
    val createdAt: Long,
    val phoneNumber: String,
    val contactName: String?
)
