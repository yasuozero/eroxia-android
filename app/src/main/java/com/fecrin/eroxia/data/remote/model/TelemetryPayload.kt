package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TelemetryMessage(
    val type: String,
    val payload: TelemetryPayload
)

@Serializable
data class TelemetryPayload(
    val t: Float,
    val motion: MotionData,
    val process: ProcessData
)

@Serializable
data class MotionData(
    val x: Float,
    val y: Float,
    val z: Float,
    val alpha: Float
)

@Serializable
data class ProcessData(
    val temperature: Float,
    val pressure: Float,
    val flow: Float
)