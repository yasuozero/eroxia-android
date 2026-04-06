package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ServerMessage (
    val type: String,
    val payload: JsonObject
)