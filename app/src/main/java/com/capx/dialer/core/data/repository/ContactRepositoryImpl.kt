package com.capx.dialer.core.data.repository

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.capx.dialer.core.data.local.dao.FavoriteDao
import com.capx.dialer.core.data.local.entity.FavoriteEntity
import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.domain.repository.ContactRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [ContactRepository] that reads contacts
 * from the system [ContactsContract] provider and merges favorite
 * status from the local [FavoriteDao].
 *
 * All ContentResolver queries run on [Dispatchers.IO] via [flowOn].
 */
@Singleton
class ContactRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favoriteDao: FavoriteDao
) : ContactRepository {

    /**
     * Queries all phone contacts and merges each with its favorite status.
     *
     * Projection is kept minimal for performance:
     * - [Phone.CONTACT_ID] — unique contact identifier
     * - [Phone.DISPLAY_NAME] — contact display name
     * - [Phone.NUMBER] — phone number string
     * - [Phone.PHOTO_THUMBNAIL_URI] — optional thumbnail URI
     */
    override fun getContacts(): Flow<List<Contact>> = flow {
        val favoriteIds = favoriteDao.getAllFavoriteIds().first().toSet()
        val contacts = queryContacts(
            selection = null,
            selectionArgs = null,
            favoriteIds = favoriteIds
        )
        emit(contacts)
    }.flowOn(Dispatchers.IO)

    /**
     * Searches contacts whose display name or phone number matches the query.
     *
     * Uses SQL LIKE with wildcards for partial matching.
     *
     * @param query The search term to match against name or number.
     */
    override fun searchContacts(query: String): Flow<List<Contact>> = flow {
        val favoriteIds = favoriteDao.getAllFavoriteIds().first().toSet()
        val sanitized = "%$query%"
        val contacts = queryContacts(
            selection = "${Phone.DISPLAY_NAME} LIKE ? OR ${Phone.NUMBER} LIKE ?",
            selectionArgs = arrayOf(sanitized, sanitized),
            favoriteIds = favoriteIds
        )
        emit(contacts)
    }.flowOn(Dispatchers.IO)

    /**
     * Returns only contacts that the user has marked as favorites.
     *
     * Loads all contacts first then filters by the favorited ID set.
     * For very large contact lists a more targeted query could be used,
     * but in practice the favorite set is small enough that filtering
     * in-memory is efficient.
     */
    override fun getFavorites(): Flow<List<Contact>> = flow {
        val favoriteIds = favoriteDao.getAllFavoriteIds().first().toSet()
        if (favoriteIds.isEmpty()) {
            emit(emptyList())
            return@flow
        }
        val allContacts = queryContacts(
            selection = null,
            selectionArgs = null,
            favoriteIds = favoriteIds
        )
        emit(allContacts.filter { it.isFavorite })
    }.flowOn(Dispatchers.IO)

    /**
     * Looks up a single contact by phone number.
     *
     * The system normalizes numbers using [Phone.NORMALIZED_NUMBER] when
     * available; we fall back to a LIKE match on the raw number column.
     *
     * @param number The phone number to search for.
     */
    override suspend fun getContactByNumber(number: String): Contact? {
        val favoriteIds = favoriteDao.getAllFavoriteIds().first().toSet()
        val contacts = queryContacts(
            selection = "${Phone.NUMBER} = ? OR ${Phone.NORMALIZED_NUMBER} = ?",
            selectionArgs = arrayOf(number, number),
            favoriteIds = favoriteIds
        )
        return contacts.firstOrNull()
    }

    /**
     * Toggles the favorite state for a given contact.
     *
     * If the contact is already favorited it will be removed;
     * otherwise it will be added to the favorites table.
     *
     * @param contactId The system contact ID to toggle.
     */
    override suspend fun toggleFavorite(contactId: Long) {
        if (favoriteDao.isFavorite(contactId)) {
            favoriteDao.deleteFavorite(contactId)
        } else {
            favoriteDao.insertFavorite(FavoriteEntity(contactId = contactId))
        }
    }

    // ──────────────────────────────────────────────────────────────
    //  Private helpers
    // ──────────────────────────────────────────────────────────────

    /**
     * Executes a content-resolver query against [Phone.CONTENT_URI] and
     * maps rows to [Contact] domain objects.
     *
     * Contacts are de-duplicated by [Phone.CONTACT_ID] so a single
     * contact with multiple numbers appears only once (first number wins).
     * Results are sorted alphabetically by display name.
     */
    private fun queryContacts(
        selection: String?,
        selectionArgs: Array<String>?,
        favoriteIds: Set<Long>
    ): List<Contact> {
        val projection = arrayOf(
            Phone.CONTACT_ID,
            Phone.DISPLAY_NAME,
            Phone.NUMBER,
            Phone.PHOTO_THUMBNAIL_URI
        )

        val contactMap = linkedMapOf<Long, Contact>()

        context.contentResolver.query(
            Phone.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${Phone.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(Phone.CONTACT_ID)
            val nameIdx = cursor.getColumnIndexOrThrow(Phone.DISPLAY_NAME)
            val numberIdx = cursor.getColumnIndexOrThrow(Phone.NUMBER)
            val photoIdx = cursor.getColumnIndexOrThrow(Phone.PHOTO_THUMBNAIL_URI)

            while (cursor.moveToNext()) {
                val contactId = cursor.getLong(idIdx)
                if (contactMap.containsKey(contactId)) continue

                val name = cursor.getString(nameIdx).orEmpty()
                val number = cursor.getString(numberIdx).orEmpty()
                val photoUri = cursor.getString(photoIdx)

                contactMap[contactId] = Contact(
                    id = contactId,
                    name = name,
                    phoneNumber = number,
                    photoUri = photoUri,
                    isFavorite = contactId in favoriteIds
                )
            }
        }

        return contactMap.values.toList()
    }
}
