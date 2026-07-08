package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Streams the call history for a single phone number — used by the call log
 * detail screen.
 */
class GetRecentCallsByNumberUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    operator fun invoke(number: String): Flow<List<RecentCall>> =
        callLogRepository.getRecentCallsByNumber(number)
}
