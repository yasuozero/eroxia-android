package com.fecrin.eroxia.di

import com.fecrin.eroxia.data.remote.MessageRouter
import com.fecrin.eroxia.data.remote.WebSocketService
import com.fecrin.eroxia.data.repository.ConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerConnectionRepository(wss: WebSocketService, router: MessageRouter): ConnectionRepository {
        return ConnectionRepository(wss, router)
    }

}