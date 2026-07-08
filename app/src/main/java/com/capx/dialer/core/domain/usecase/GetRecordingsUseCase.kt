package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.Recording
import com.capx.dialer.core.domain.repository.RecordingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecordingsUseCase @Inject constructor(
    private val recordingRepository: RecordingRepository
) {
    operator fun invoke(): Flow<List<Recording>> {
        return recordingRepository.getRecordings()
    }
}
