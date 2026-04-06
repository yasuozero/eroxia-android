package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminAuthPayload(
    val password: String
)