package com.capx.dialer.di

import android.content.Context
import com.capx.dialer.core.domain.repository.TelecomBridge
import com.capx.dialer.core.telecom.CallManager
import com.capx.dialer.core.telecom.TelecomBridgeImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TelecomModule {

    @Provides
    @Singleton
    fun provideCallManager(@ApplicationContext context: Context): CallManager {
        return CallManager(context)
    }

    @Provides
    @Singleton
    fun provideTelecomBridge(
        @ApplicationContext context: Context,
        callManager: CallManager
    ): TelecomBridge {
        return TelecomBridgeImpl(context, callManager)
    }
}
