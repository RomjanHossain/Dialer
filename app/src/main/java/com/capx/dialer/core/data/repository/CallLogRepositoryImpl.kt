package com.capx.dialer.core.data.repository

import android.content.Context
import android.provider.CallLog
import com.capx.dialer.core.domain.model.CallType
import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.repository.CallLogRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [CallLogRepository] that reads the device
 * call history from [CallLog.Calls] via [android.content.ContentResolver].
 *
 * Results are always sorted newest-first and capped at [MAX_ENTRIES]
 * to avoid loading the entire history into memory.
 */
@Singleton
class CallLogRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CallLogRepository {

    companion object {
        /** Maximum number of call-log entries loaded per query. */
        private const val MAX_ENTRIES = 500
    }

    /**
     * Emits the most recent call log entries mapped to [RecentCall] domain objects.
     *
     * Projection columns:
     * - [CallLog.Calls._ID] — unique row identifier
     * - [CallLog.Calls.NUMBER] — raw phone number
     * - [CallLog.Calls.CACHED_NAME] — contact name cached by the system
     * - [CallLog.Calls.TYPE] — incoming / outgoing / missed / etc.
     * - [CallLog.Calls.DATE] — epoch millis of the call
     * - [CallLog.Calls.DURATION] — duration in seconds
     */
    override fun getRecentCalls(): Flow<List<RecentCall>> = flow {
        val calls = mutableListOf<RecentCall>()

        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(CallLog.Calls._ID)
            val numberIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
            val nameIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)
            val typeIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)
            val dateIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DATE)
            val durationIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)

            var count = 0
            while (cursor.moveToNext() && count < MAX_ENTRIES) {
                val id = cursor.getLong(idIdx)
                val number = cursor.getString(numberIdx).orEmpty()
                val name = cursor.getString(nameIdx)
                val type = cursor.getInt(typeIdx)
                val date = cursor.getLong(dateIdx)
                val duration = cursor.getLong(durationIdx)

                calls.add(
                    RecentCall(
                        id = id,
                        number = number,
                        contactName = name,
                        callType = mapCallType(type),
                        timestamp = date,
                        durationSeconds = duration
                    )
                )
                count++
            }
        }

        emit(calls)
    }.flowOn(Dispatchers.IO)

    // ──────────────────────────────────────────────────────────────
    //  Private helpers
    // ──────────────────────────────────────────────────────────────

    /**
     * Maps the system [CallLog.Calls.TYPE] integer constants to the
     * domain [CallType] sealed hierarchy.
     */
    private fun mapCallType(systemType: Int): CallType = when (systemType) {
        CallLog.Calls.INCOMING_TYPE -> CallType.Incoming
        CallLog.Calls.OUTGOING_TYPE -> CallType.Outgoing
        CallLog.Calls.MISSED_TYPE -> CallType.Missed
        CallLog.Calls.REJECTED_TYPE -> CallType.Rejected
        CallLog.Calls.BLOCKED_TYPE -> CallType.Blocked
        else -> CallType.Unknown
    }
}
