package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.CallLogRepository
import javax.inject.Inject

/** Clears the unread-missed-calls badge by marking missed calls as read. */
class MarkMissedCallsReadUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    suspend operator fun invoke() = callLogRepository.markMissedCallsAsRead()
}
