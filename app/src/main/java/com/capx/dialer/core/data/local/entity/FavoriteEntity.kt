package com.capx.dialer.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a favorited contact.
 *
 * Favorites are stored locally so they persist across sessions without
 * modifying the system contacts provider. The [contactId] maps to the
 * contact's identifier from [android.provider.ContactsContract].
 *
 * @property contactId System contact ID, used as the primary key (no duplicates).
 * @property addedAt Epoch timestamp (millis) when the contact was favorited.
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val contactId: Long,
    val addedAt: Long = System.currentTimeMillis()
)
