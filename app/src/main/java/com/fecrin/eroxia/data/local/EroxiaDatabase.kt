package com.fecrin.eroxia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fecrin.eroxia.data.local.dao.TelemetryDao
import com.fecrin.eroxia.data.local.entity.TelemetryEntity

@Database(entities = [TelemetryEntity::class], version = 1, exportSchema = false)
abstract class EroxiaDatabase : RoomDatabase() {
    abstract fun telemetryDao(): TelemetryDao
}