package com.capx.dialer.di

import android.content.Context
import androidx.room.Room
import com.capx.dialer.core.data.local.DialerDatabase
import com.capx.dialer.core.data.local.dao.FavoriteDao
import com.capx.dialer.core.data.local.dao.RecordingDao
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
    fun provideDialerDatabase(@ApplicationContext context: Context): DialerDatabase {
        return Room.databaseBuilder(
            context,
            DialerDatabase::class.java,
            DialerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: DialerDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideRecordingDao(database: DialerDatabase): RecordingDao {
        return database.recordingDao()
    }

    @Provides
    @Singleton
    fun provideContactRepository(
        @ApplicationContext context: Context,
        favoriteDao: FavoriteDao
    ): ContactRepository {
        return ContactRepositoryImpl(context, favoriteDao)
    }

    @Provides
    @Singleton
    fun provideCallLogRepository(@ApplicationContext context: Context): CallLogRepository {
        return CallLogRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideRecordingRepository(recordingDao: RecordingDao): RecordingRepository {
        return RecordingRepositoryImpl(recordingDao)
    }
}
