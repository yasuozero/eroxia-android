package com.fecrin.eroxia.data.remote

import com.fecrin.eroxia.data.json
import com.fecrin.eroxia.data.remote.model.ActionPayload
import com.fecrin.eroxia.data.remote.model.AdminAuthResultPayload
import com.fecrin.eroxia.data.remote.model.HandshakePayload
import com.fecrin.eroxia.data.remote.model.ServerMessage
import com.fecrin.eroxia.data.remote.model.TelemetryPayload
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

class MessageRouter @Inject constructor() {

    private val _handshake = MutableSharedFlow<HandshakePayload>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _admin = MutableSharedFlow<AdminAuthResultPayload>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _telemetry = MutableSharedFlow<TelemetryPayload>(
        extraBufferCapacity = 20, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _action = MutableSharedFlow<ActionPayload>(
        extraBufferCapacity = 3, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val handshake: SharedFlow<HandshakePayload> = _handshake.asSharedFlow()
    val telemetry: SharedFlow<TelemetryPayload> = _telemetry.asSharedFlow()
    val admin: SharedFlow<AdminAuthResultPayload> = _admin.asSharedFlow()

    val action: SharedFlow<ActionPayload> = _action.asSharedFlow()



    fun route(message: ServerMessage) {
        when (message.type) {
            "handshake" -> {
                val payload = json.decodeFromJsonElement<HandshakePayload>(message.payload)
                _handshake.tryEmit(payload)
            }

            "admin_result" -> {
                val payload = json.decodeFromJsonElement<AdminAuthResultPayload>(message.payload)
                _admin.tryEmit(payload)
            }

            "telemetry" -> {
                val payload = json.decodeFromJsonElement<TelemetryPayload>(message.payload)
                _telemetry.tryEmit(payload)
            }

            "action_result" -> {
                val payload = json.decodeFromJsonElement<ActionPayload>(message.payload)
                _action.tryEmit(payload)
            }
        }
    }

}