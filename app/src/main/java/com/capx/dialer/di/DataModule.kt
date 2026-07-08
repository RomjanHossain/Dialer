package com.capx.dialer.di

import android.content.Context
import com.capx.dialer.core.data.repository.CallLogRepositoryImpl
import com.capx.dialer.core.data.repository.ContactRepositoryImpl
import com.capx.dialer.core.data.repository.RecordingRepositoryImpl
import com.capx.dialer.core.domain.repository.CallLogRepository
import com.capx.dialer.core.domain.repository.ContactRepository
import com.capx.dialer.core.domain.repository.RecordingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideContactRepository(@ApplicationContext context: Context): ContactRepository {
        return ContactRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideCallLogRepository(@ApplicationContext context: Context): CallLogRepository {
        return CallLogRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideRecordingRepository(): RecordingRepository {
        return RecordingRepositoryImpl()
    }
}
