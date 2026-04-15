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

    @Query("SELECT DISTINCT session_id FROM telemetry_data ORDER BY id DESC")
    fun getAllSessionIds(): Flow<List<String>>

    @Query("SELECT * FROM telemetry_data WHERE session_id = :sessionId ORDER BY time ASC")
    suspend fun getTelemetryForPlayback(sessionId: String): List<TelemetryEntity>;

    @Query("DELETE FROM telemetry_data")
    suspend fun clearAllTelemetry()
}