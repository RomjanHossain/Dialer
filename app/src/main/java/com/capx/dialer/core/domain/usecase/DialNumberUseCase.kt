package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.TelecomBridge
import javax.inject.Inject

class DialNumberUseCase @Inject constructor(
    private val telecomBridge: TelecomBridge
) {
    /**
     * Places a call to [number], optionally on a specific SIM [accountId].
     *
     * @param number Phone number to dial.
     * @param accountId Optional SIM account id; null uses the platform default.
     */
    operator fun invoke(number: String, accountId: String? = null): Result<Unit> {
        if (number.isBlank()) {
            return Result.failure(IllegalArgumentException("Number cannot be empty"))
        }
        telecomBridge.placeCall(number, accountId)
        return Result.success(Unit)
    }
}
