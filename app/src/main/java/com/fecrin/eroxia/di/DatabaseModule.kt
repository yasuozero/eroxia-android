package com.fecrin.eroxia.di

import android.content.Context
import androidx.room.Room
import com.fecrin.eroxia.data.local.EroxiaDatabase
import com.fecrin.eroxia.data.local.dao.TelemetryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EroxiaDatabase {
        return Room.databaseBuilder(
            context,
            EroxiaDatabase::class.java,
            "eroxia_database"
        ).build()
    }

    @Provides
    fun provideTelemetryDao(db: EroxiaDatabase): TelemetryDao {
        return db.telemetryDao()
    }
}