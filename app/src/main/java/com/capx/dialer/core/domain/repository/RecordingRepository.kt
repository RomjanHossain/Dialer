package com.capx.dialer.core.domain.repository

import com.capx.dialer.core.domain.model.Recording
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for call recording data access.
 *
 * Manages persistence and retrieval of call recording metadata.
 * The actual audio files are stored on disk; this repository handles
 * the metadata entries that reference those files.
 *
 * This interface lives in the domain layer and must have ZERO Android dependencies.
 */
interface RecordingRepository {

    /**
     * Observes all recordings, ordered by most recent first.
     *
     * @return A [Flow] emitting the current list of all recordings.
     */
    fun getRecordings(): Flow<List<Recording>>

    /**
     * Observes a single recording by its [id].
     *
     * @param id The unique identifier of the recording.
     * @return A [Flow] emitting the recording if found, or null.
     */
    fun getRecordingById(id: Long): Flow<Recording?>

    /**
     * Persists a new recording entry.
     *
     * @param recording The [Recording] metadata to save.
     * @return The auto-generated ID of the newly saved recording.
     */
    suspend fun saveRecording(recording: Recording): Long

    /**
     * Deletes a recording entry and its associated audio file.
     *
     * @param id The unique identifier of the recording to delete.
     */
    suspend fun deleteRecording(id: Long)

    /**
     * Observes the recording associated with a specific call log entry.
     *
     * @param callId The ID of the call log entry.
     * @return A [Flow] emitting the associated recording, or null if none exists.
     */
    fun getRecordingByCallId(callId: Long): Flow<Recording?>
}
