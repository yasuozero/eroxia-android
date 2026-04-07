package com.fecrin.eroxia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fecrin.eroxia.data.local.entity.TelemetryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TelemetryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTelemetry(telemetry: TelemetryEntity)

    @Query("SELECT * FROM telemetry_data ORDER BY id DESC")
    fun getAllTelemetry(): Flow<List<TelemetryEntity>>

    @Query("SELECT * FROM telemetry_data WHERE sessionId = :sessionId ORDER BY id ASC")
    fun getTelemetryBySession(sessionId: String): Flow<List<TelemetryEntity>>

    @Query("SELECT DISTINCT sessionId FROM telemetry_data ORDER BY id DESC")
    fun getAllSessionIds(): Flow<List<String>>

    @Query("DELETE FROM telemetry_data")
    suspend fun clearAllTelemetry()
}