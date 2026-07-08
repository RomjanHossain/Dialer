package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    operator fun invoke(): Flow<List<Contact>> {
        return contactRepository.getContacts()
    }
}
