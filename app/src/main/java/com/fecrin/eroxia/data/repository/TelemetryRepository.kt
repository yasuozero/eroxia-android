package com.fecrin.eroxia.data.repository

import com.fecrin.eroxia.data.local.dao.TelemetryDao
import com.fecrin.eroxia.data.local.entity.TelemetryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TelemetryRepository @Inject constructor(private val dao: TelemetryDao) {

    suspend fun saveTelemetry(telemetry: TelemetryEntity) {
        dao.insertTelemetry(telemetry)
    }

    fun getSessions(): Flow<List<String>> {
        return dao.getAllSessionIds()
    }

    suspend fun getPlaybackData(sessionId: String): List<TelemetryEntity> {
        return dao.getTelemetryForPlayback(sessionId)
    }

}