package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Streams the number of unread missed calls for the recents tab badge. */
class GetMissedCallCountUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    operator fun invoke(): Flow<Int> = callLogRepository.getMissedCount()
}
