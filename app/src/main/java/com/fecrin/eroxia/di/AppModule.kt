package com.fecrin.eroxia.di

import android.content.Context
import androidx.room.Room
import com.fecrin.eroxia.data.local.EroxiaDatabase
import com.fecrin.eroxia.data.local.dao.TelemetryDao
import com.fecrin.eroxia.data.remote.MessageRouter
import com.fecrin.eroxia.data.remote.WebSocketService
import com.fecrin.eroxia.data.repository.ConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerConnectionRepository(wss: WebSocketService, router: MessageRouter,telemetryDao: TelemetryDao): ConnectionRepository {
        return ConnectionRepository(wss, router, telemetryDao)
    }

    @Provides
    @Singleton
    fun provideEroxiaDatabase(@ApplicationContext context: Context): EroxiaDatabase {
        return Room.databaseBuilder(
            context,
            EroxiaDatabase::class.java,
            "eroxia_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTelemetryDao(database: EroxiaDatabase): TelemetryDao {
        return database.telemetryDao()
    }
}