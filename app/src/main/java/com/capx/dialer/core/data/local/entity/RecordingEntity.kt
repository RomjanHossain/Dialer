package com.capx.dialer.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordings")
data class RecordingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val callId: Long? = null,
    val filePath: String,
    val duration: Long,
    val timestamp: Long,
    val contactName: String? = null,
    val contactNumber: String? = null,
    val fileSize: Long = 0,
    val isEncrypted: Boolean = false
)
