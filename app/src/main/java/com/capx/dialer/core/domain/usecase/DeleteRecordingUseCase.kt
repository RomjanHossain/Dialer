package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.RecordingRepository
import javax.inject.Inject

class DeleteRecordingUseCase @Inject constructor(
    private val recordingRepository: RecordingRepository
) {
    suspend operator fun invoke(id: Long) {
        recordingRepository.deleteRecording(id)
    }
}
