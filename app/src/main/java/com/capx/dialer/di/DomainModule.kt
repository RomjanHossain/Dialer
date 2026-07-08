package com.capx.dialer.di

import com.capx.dialer.core.domain.repository.CallLogRepository
import com.capx.dialer.core.domain.repository.ContactRepository
import com.capx.dialer.core.domain.repository.RecordingRepository
import com.capx.dialer.core.domain.repository.TelecomBridge
import com.capx.dialer.core.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideDialNumberUseCase(telecomBridge: TelecomBridge): DialNumberUseCase {
        return DialNumberUseCase(telecomBridge)
    }

    @Provides
    fun provideGetContactsUseCase(contactRepository: ContactRepository): GetContactsUseCase {
        return GetContactsUseCase(contactRepository)
    }

    @Provides
    fun provideSearchContactsUseCase(contactRepository: ContactRepository): SearchContactsUseCase {
        return SearchContactsUseCase(contactRepository)
    }

    @Provides
    fun provideGetFavoritesUseCase(contactRepository: ContactRepository): GetFavoritesUseCase {
        return GetFavoritesUseCase(contactRepository)
    }

    @Provides
    fun provideToggleFavoriteUseCase(contactRepository: ContactRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(contactRepository)
    }

    @Provides
    fun provideGetRecentCallsUseCase(callLogRepository: CallLogRepository): GetRecentCallsUseCase {
        return GetRecentCallsUseCase(callLogRepository)
    }

    @Provides
    fun provideDeleteRecentCallUseCase(callLogRepository: CallLogRepository): DeleteRecentCallUseCase {
        return DeleteRecentCallUseCase(callLogRepository)
    }

    @Provides
    fun provideObserveActiveCallUseCase(telecomBridge: TelecomBridge): ObserveActiveCallUseCase {
        return ObserveActiveCallUseCase(telecomBridge)
    }

    @Provides
    fun provideGetRecordingsUseCase(recordingRepository: RecordingRepository): GetRecordingsUseCase {
        return GetRecordingsUseCase(recordingRepository)
    }

    @Provides
    fun provideDeleteRecordingUseCase(recordingRepository: RecordingRepository): DeleteRecordingUseCase {
        return DeleteRecordingUseCase(recordingRepository)
    }
}
