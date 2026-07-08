package com.capx.dialer.core.ui.util

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

/**
 * Helpers that launch the system Contacts app for viewing or creating a
 * contact. Kept out of the composables so the UI just calls a single function.
 */
object ContactIntents {

    /**
     * Opens the given [number] in the system Contacts app: shows the existing
     * contact if the number is saved, otherwise opens the "create contact"
     * form pre-filled with the number.
     */
    fun viewOrCreate(context: Context, number: String) {
        val contactUri = lookupContactUri(context, number)
        if (contactUri != null) {
            runCatching {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, contactUri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }.onFailure { create(context, number) }
        } else {
            create(context, number)
        }
    }

    /** Opens the system "create new contact" form pre-filled with [number]. */
    fun create(context: Context, number: String) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(intent) }
    }

    /** Returns a viewable contact Uri for [number], or null if not saved. */
    private fun lookupContactUri(context: Context, number: String): Uri? {
        if (number.isBlank()) return null
        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        return try {
            context.contentResolver.query(
                lookupUri,
                arrayOf(ContactsContract.PhoneLookup._ID),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(
                        cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID)
                    )
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id)
                } else {
                    null
                }
            }
        } catch (e: SecurityException) {
            null
        }
    }
}
