package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HandshakePayload(
    val admin: Boolean, @SerialName("session_id") val sessionId: String, val system: SystemState
)

@Serializable
data class SystemState(
    val active: Boolean, val dust: Boolean, val air: Boolean
)