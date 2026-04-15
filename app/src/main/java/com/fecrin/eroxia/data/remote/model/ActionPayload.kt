package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ActionPayload(
    val action: String,
    val success: Boolean
)