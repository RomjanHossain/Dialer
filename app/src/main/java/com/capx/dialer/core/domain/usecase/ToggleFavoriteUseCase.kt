package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.repository.ContactRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(contactId: Long) {
        contactRepository.toggleFavorite(contactId)
    }
}
