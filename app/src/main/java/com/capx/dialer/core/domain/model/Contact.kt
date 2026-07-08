package com.capx.dialer.core.domain.model

/**
 * Domain model representing a phone contact.
 *
 * This is a pure Kotlin data class with no Android dependencies.
 * It serves as the single source of truth for contact information
 * across all layers of the application.
 *
 * @property id Unique identifier for the contact (maps to ContactsContract ID).
 * @property name Display name of the contact.
 * @property phoneNumber Primary phone number associated with this contact.
 * @property phoneLabel Label for the phone number (e.g., "Mobile", "Home", "Work").
 * @property photoUri URI string pointing to the contact's photo, if available.
 * @property isFavorite Whether this contact is marked as a favorite by the user.
 * @property isStarred Whether this contact is starred in the system contacts.
 * @property lookupKey Stable lookup key for the contact, used for persistent references.
 */
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String,
    val phoneLabel: String = "",
    val photoUri: String? = null,
    val isFavorite: Boolean = false,
    val isStarred: Boolean = false,
    val lookupKey: String = ""
)
