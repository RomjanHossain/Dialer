package com.capx.dialer.core.data.local

import com.capx.dialer.core.data.local.entity.RecordingEntity
import com.capx.dialer.core.domain.model.Recording

/**
 * Mapping extensions between Room entities and domain models.
 *
 * These live in the data layer so the domain layer stays free of
 * any Room / Android framework dependencies.
 */

/**
 * Converts a [RecordingEntity] (database row) to the domain [Recording] model.
 */
fun RecordingEntity.toDomain(): Recording = Recording(
    id = id,
    callId = callId,
    filePath = filePath,
    durationMs = durationMs,
    sizeBytes = sizeBytes,
    createdAt = createdAt,
    phoneNumber = phoneNumber,
    contactName = contactName
)

/**
 * Converts a domain [Recording] model to a [RecordingEntity] for persistence.
 *
 * When creating a new recording (id == 0), Room will auto-generate the ID.
 */
fun Recording.toEntity(): RecordingEntity = RecordingEntity(
    id = id,
    callId = callId,
    filePath = filePath,
    durationMs = durationMs,
    sizeBytes = sizeBytes,
    createdAt = createdAt,
    phoneNumber = phoneNumber,
    contactName = contactName
)
