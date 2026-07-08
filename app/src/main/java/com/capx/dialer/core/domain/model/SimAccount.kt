package com.capx.dialer.core.domain.model

/**
 * Domain model representing a call-capable SIM / phone account.
 *
 * Pure Kotlin, decoupled from Android's `PhoneAccountHandle`. The [id]
 * is an opaque, stable identifier that the telecom layer can map back to
 * the real platform handle when placing a call.
 *
 * @property id Opaque stable identifier for the underlying phone account.
 * @property label Human-friendly label (carrier name or "SIM 1").
 * @property slotIndex Zero-based SIM slot index, or -1 if unknown.
 */
data class SimAccount(
    val id: String,
    val label: String,
    val slotIndex: Int = -1
)
