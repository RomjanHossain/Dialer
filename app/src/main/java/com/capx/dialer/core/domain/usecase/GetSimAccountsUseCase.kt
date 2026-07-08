package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.SimAccount
import com.capx.dialer.core.domain.repository.TelecomBridge
import javax.inject.Inject

/**
 * Returns the call-capable SIM accounts on the device so the UI can decide
 * whether to show a SIM chooser before placing a call.
 */
class GetSimAccountsUseCase @Inject constructor(
    private val telecomBridge: TelecomBridge
) {
    operator fun invoke(): List<SimAccount> = telecomBridge.getSimAccounts()
}
