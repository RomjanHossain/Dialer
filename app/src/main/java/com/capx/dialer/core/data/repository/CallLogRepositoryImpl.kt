package com.capx.dialer.core.data.repository

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import com.capx.dialer.core.domain.model.CallType
import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.repository.CallLogRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [CallLogRepository] that reads the device
 * call history from [CallLog.Calls] via [android.content.ContentResolver].
 *
 * [getRecentCalls] registers a [ContentObserver] so the list updates live
 * whenever a call is placed or received. Results are sorted newest-first and
 * capped at [MAX_ENTRIES].
 */
@Singleton
class CallLogRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CallLogRepository {

    companion object {
        /** Maximum number of call-log entries loaded per query. */
        private const val MAX_ENTRIES = 500

        private val PROJECTION = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.CACHED_PHOTO_URI,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )
    }

    override fun getRecentCalls(): Flow<List<RecentCall>> = callbackFlow {
        val producer = this
        // Emit immediately, then re-emit whenever the call log changes.
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                // Query off the observer's (main-looper) thread.
                producer.launch { producer.trySend(query(null, null)) }
            }
        }
        context.contentResolver.registerContentObserver(
            CallLog.Calls.CONTENT_URI, true, observer
        )
        producer.trySend(query(null, null))
        awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }.flowOn(Dispatchers.IO)

    override fun getRecentCallsByNumber(number: String): Flow<List<RecentCall>> = flow {
        emit(query("${CallLog.Calls.NUMBER} = ?", arrayOf(number)))
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteRecentCall(id: Long) {
        try {
            context.contentResolver.delete(
                CallLog.Calls.CONTENT_URI,
                "${CallLog.Calls._ID} = ?",
                arrayOf(id.toString())
            )
        } catch (e: SecurityException) {
            // Missing WRITE_CALL_LOG — ignore.
        }
    }

    override suspend fun deleteAllRecentCalls() {
        try {
            context.contentResolver.delete(CallLog.Calls.CONTENT_URI, null, null)
        } catch (e: SecurityException) {
            // Missing WRITE_CALL_LOG — ignore.
        }
    }

    // ──────────────────────────────────────────────────────────────

    /** Runs a call-log query and maps rows to [RecentCall] domain objects. */
    private fun query(selection: String?, selectionArgs: Array<String>?): List<RecentCall> {
        val calls = mutableListOf<RecentCall>()
        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            PROJECTION,
            selection,
            selectionArgs,
            "${CallLog.Calls.DATE} DESC"
        )?.use { cursor ->
            val idIdx = cursor.getColumnIndexOrThrow(CallLog.Calls._ID)
            val numberIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
            val nameIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME)
            val photoIdx = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI)
            val typeIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)
            val dateIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DATE)
            val durationIdx = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)

            var count = 0
            while (cursor.moveToNext() && count < MAX_ENTRIES) {
                calls.add(
                    RecentCall(
                        id = cursor.getLong(idIdx),
                        number = cursor.getString(numberIdx).orEmpty(),
                        contactName = cursor.getString(nameIdx)?.takeIf { it.isNotBlank() },
                        contactPhotoUri = if (photoIdx >= 0) cursor.getString(photoIdx) else null,
                        type = mapCallType(cursor.getInt(typeIdx)),
                        timestamp = cursor.getLong(dateIdx),
                        duration = cursor.getLong(durationIdx)
                    )
                )
                count++
            }
        }
        return calls
    }

    private fun mapCallType(systemType: Int): CallType = when (systemType) {
        CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
        CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
        CallLog.Calls.MISSED_TYPE -> CallType.MISSED
        CallLog.Calls.REJECTED_TYPE -> CallType.REJECTED
        CallLog.Calls.BLOCKED_TYPE -> CallType.BLOCKED
        else -> CallType.INCOMING
    }
}
