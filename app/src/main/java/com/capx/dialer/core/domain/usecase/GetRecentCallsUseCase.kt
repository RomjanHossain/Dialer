package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.RecentCall
import com.capx.dialer.core.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentCallsUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    operator fun invoke(): Flow<List<RecentCall>> {
        return callLogRepository.getRecentCalls()
    }
}
