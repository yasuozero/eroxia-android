package com.fecrin.eroxia.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fecrin.eroxia.data.remote.model.MotionData
import com.fecrin.eroxia.data.remote.model.ProcessData

@Entity(tableName = "telemetry_data")
data class TelemetryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "session_id")
    val sessionId: String,

    @ColumnInfo(name = "time")
    val t: Float,

    @Embedded(prefix = "motion_")
    val motion: MotionData,

    @Embedded(prefix = "process_")
    val process: ProcessData
)