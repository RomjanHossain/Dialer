package com.capx.dialer.core.domain.repository

import com.capx.dialer.core.domain.model.RecentCall
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for call log (recents) data access.
 *
 * Provides reactive streams of call history and mutation operations
 * for managing call log entries. Implementations in the data layer
 * will bridge to the system's CallLog content provider.
 *
 * This interface lives in the domain layer and must have ZERO Android dependencies.
 */
interface CallLogRepository {

    /**
     * Observes the complete call history, ordered by most recent first.
     *
     * @return A [Flow] emitting the current list of recent calls,
     *         re-emitting whenever the call log changes.
     */
    fun getRecentCalls(): Flow<List<RecentCall>>

    /**
     * Observes call history filtered to a specific phone [number].
     *
     * Useful for displaying call history in a contact detail view.
     *
     * @param number The phone number to filter by.
     * @return A [Flow] emitting recent calls matching the given number.
     */
    fun getRecentCallsByNumber(number: String): Flow<List<RecentCall>>

    /**
     * Deletes a single call log entry by its [id].
     *
     * @param id The unique identifier of the call log entry to delete.
     */
    suspend fun deleteRecentCall(id: Long)

    /**
     * Deletes all call log entries.
     *
     * This is a destructive operation and should be confirmed by the user
     * before invocation.
     */
    suspend fun deleteAllRecentCalls()
}
