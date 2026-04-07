package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CommandPayload(
    val action: String
)