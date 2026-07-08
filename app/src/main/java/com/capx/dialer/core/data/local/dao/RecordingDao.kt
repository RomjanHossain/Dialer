package com.capx.dialer.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capx.dialer.core.data.local.entity.RecordingEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the [RecordingEntity] table.
 *
 * All read queries return [Flow] for reactive observation. Write
 * operations are suspend functions to be called from coroutines.
 */
@Dao
interface RecordingDao {

    /**
     * Observes all recordings ordered by creation date (newest first).
     */
    @Query("SELECT * FROM recordings ORDER BY timestamp DESC")
    fun getAllRecordings(): Flow<List<RecordingEntity>>

    /**
     * Observes a single recording by its primary key.
     *
     * @param id The recording's auto-generated ID.
     * @return A [Flow] emitting the entity, or `null` if not found.
     */
    @Query("SELECT * FROM recordings WHERE id = :id")
    fun getRecordingById(id: Long): Flow<RecordingEntity?>

    /**
     * Observes the recording associated with a specific call.
     *
     * @param callId The call identifier to look up.
     * @return A [Flow] emitting the entity, or `null` if no recording exists.
     */
    @Query("SELECT * FROM recordings WHERE callId = :callId")
    fun getRecordingByCallId(callId: Long): Flow<RecordingEntity?>

    /**
     * Inserts or replaces a recording entry.
     *
     * @param recording The entity to persist.
     * @return The row ID of the inserted entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecording(recording: RecordingEntity): Long

    /**
     * Deletes a recording by its primary key.
     *
     * @param id The recording ID to remove.
     */
    @Query("DELETE FROM recordings WHERE id = :id")
    suspend fun deleteRecording(id: Long)
}
