package com.capx.dialer.core.data.repository

import com.capx.dialer.core.data.local.dao.RecordingDao
import com.capx.dialer.core.data.local.toDomain
import com.capx.dialer.core.data.local.toEntity
import com.capx.dialer.core.domain.model.Recording
import com.capx.dialer.core.domain.repository.RecordingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [RecordingRepository] backed by Room.
 *
 * All operations delegate to [RecordingDao] with entity ↔ domain
 * mapping handled by the extension functions in `EntityMappers.kt`.
 */
@Singleton
class RecordingRepositoryImpl @Inject constructor(
    private val recordingDao: RecordingDao
) : RecordingRepository {

    /**
     * Observes all recordings, mapped to domain models.
     */
    override fun getAllRecordings(): Flow<List<Recording>> =
        recordingDao.getAllRecordings().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Observes a single recording by ID.
     *
     * @param id The recording primary key.
     */
    override fun getRecordingById(id: Long): Flow<Recording?> =
        recordingDao.getRecordingById(id).map { it?.toDomain() }

    /**
     * Observes the recording associated with a specific call.
     *
     * @param callId The call identifier.
     */
    override fun getRecordingByCallId(callId: Long): Flow<Recording?> =
        recordingDao.getRecordingByCallId(callId).map { it?.toDomain() }

    /**
     * Persists a recording and returns the generated row ID.
     *
     * @param recording The domain model to store.
     * @return The auto-generated ID assigned by Room.
     */
    override suspend fun insertRecording(recording: Recording): Long =
        recordingDao.insertRecording(recording.toEntity())

    /**
     * Removes a recording by its primary key.
     *
     * @param id The recording ID to delete.
     */
    override suspend fun deleteRecording(id: Long) =
        recordingDao.deleteRecording(id)
}
