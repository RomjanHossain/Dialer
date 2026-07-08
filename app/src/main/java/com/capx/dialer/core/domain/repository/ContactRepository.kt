package com.capx.dialer.core.domain.repository

import com.capx.dialer.core.domain.model.Contact
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for contact data access.
 *
 * Implementations are responsible for sourcing contact data from
 * the system contacts provider or any other backing store.
 * All reactive streams are exposed as [Flow] for lifecycle-aware collection.
 *
 * This interface lives in the domain layer and must have ZERO Android dependencies.
 * Concrete implementations in the data layer will handle Android-specific APIs.
 */
interface ContactRepository {

    /**
     * Observes the full list of contacts, sorted alphabetically by name.
     *
     * @return A [Flow] emitting the current list of contacts,
     *         re-emitting whenever the underlying data changes.
     */
    fun getContacts(): Flow<List<Contact>>

    /**
     * Searches contacts by name or phone number using the given [query].
     *
     * The search is case-insensitive and supports partial matching.
     *
     * @param query The search string to filter contacts by.
     * @return A [Flow] emitting the filtered list of matching contacts.
     */
    fun searchContacts(query: String): Flow<List<Contact>>

    /**
     * Observes the list of contacts marked as favorites.
     *
     * @return A [Flow] emitting the current list of favorite contacts.
     */
    fun getFavorites(): Flow<List<Contact>>

    /**
     * Looks up a single contact by their phone number.
     *
     * This is a one-shot lookup (not reactive) used for caller-ID resolution
     * during incoming calls where immediate resolution is needed.
     *
     * @param number The phone number to look up.
     * @return The matching [Contact], or null if no contact is found.
     */
    suspend fun getContactByNumber(number: String): Contact?

    /**
     * Toggles the favorite status of the contact with the given [contactId].
     *
     * If the contact is currently a favorite, it will be unfavorited, and vice versa.
     *
     * @param contactId The unique identifier of the contact to toggle.
     */
    suspend fun toggleFavorite(contactId: Long)
}
