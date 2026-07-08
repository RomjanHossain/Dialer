package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.CallState
import com.capx.dialer.core.domain.repository.TelecomBridge
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveActiveCallUseCase @Inject constructor(
    private val telecomBridge: TelecomBridge
) {
    operator fun invoke(): StateFlow<CallState> {
        return telecomBridge.callState
    }
}
