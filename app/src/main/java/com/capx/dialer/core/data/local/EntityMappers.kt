package com.capx.dialer.core.data.local

import com.capx.dialer.core.data.local.entity.RecordingEntity
import com.capx.dialer.core.domain.model.Recording

fun RecordingEntity.toDomain(): Recording = Recording(
    id = id,
    callId = callId,
    filePath = filePath,
    duration = duration,
    timestamp = timestamp,
    contactName = contactName,
    contactNumber = contactNumber,
    fileSize = fileSize,
    isEncrypted = isEncrypted
)

fun Recording.toEntity(): RecordingEntity = RecordingEntity(
    id = id,
    callId = callId,
    filePath = filePath,
    duration = duration,
    timestamp = timestamp,
    contactName = contactName,
    contactNumber = contactNumber,
    fileSize = fileSize,
    isEncrypted = isEncrypted
)
