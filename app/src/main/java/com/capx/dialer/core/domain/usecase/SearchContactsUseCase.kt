package com.capx.dialer.core.domain.usecase

import com.capx.dialer.core.domain.model.Contact
import com.capx.dialer.core.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    operator fun invoke(query: String): Flow<List<Contact>> {
        return contactRepository.searchContacts(query)
    }
    
    // T9 mapping could be used here to map number pad presses to letters for searching
}
