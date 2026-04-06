package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class AdminAuthResultPayload(
    @SerialName("session_id") val sessionId: String,
    val status: String,
)

