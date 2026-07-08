package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.TelecomBridge
import javax.inject.Inject

class DialNumberUseCase @Inject constructor(
    private val telecomBridge: TelecomBridge
) {
    operator fun invoke(number: String): Result<Unit> {
        if (number.isBlank()) return Result.failure(IllegalArgumentException("Number cannot be empty"))
        
        // Basic validation could go here
        
        telecomBridge.placeCall(number)
        return Result.success(Unit)
    }
}
