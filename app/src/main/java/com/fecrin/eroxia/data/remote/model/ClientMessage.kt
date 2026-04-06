package com.fecrin.eroxia.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientMessage<T> (
    val type: String,
    val payload: T
)