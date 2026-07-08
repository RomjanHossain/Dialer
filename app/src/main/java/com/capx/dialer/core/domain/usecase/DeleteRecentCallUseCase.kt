package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.CallLogRepository
import javax.inject.Inject

class DeleteRecentCallUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    suspend operator fun invoke(id: Long) {
        callLogRepository.deleteRecentCall(id)
    }
}
