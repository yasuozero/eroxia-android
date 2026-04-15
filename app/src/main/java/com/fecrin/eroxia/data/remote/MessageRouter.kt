package com.fecrin.eroxia.data.remote

import android.util.Log
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
import kotlinx.serialization.json.JsonElement
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
            ServerMessageTypes.HANDSHAKE -> tryDecodeAndEmit(message.payload, _handshake, "Handshake")

            ServerMessageTypes.ADMIN_RESULT -> tryDecodeAndEmit(message.payload, _admin, "Admin")

            ServerMessageTypes.TELEMETRY -> tryDecodeAndEmit(message.payload, _telemetry, "Telemetry")

            ServerMessageTypes.ACTION_RESULT -> tryDecodeAndEmit(message.payload, _action, "Action")

            else -> Log.w("MessageRouter", "Unknown message type received: ${message.type}")
        }
    }

}

private inline fun <reified T> tryDecodeAndEmit(
    payloadElement: JsonElement,
    flow: MutableSharedFlow<T>,
    payloadName: String
) {
    try {
        val payload = json.decodeFromJsonElement<T>(payloadElement)
        flow.tryEmit(payload)
    } catch (e: Exception) {
        Log.e("MessageRouter", "Failed to parse $payloadName payload: ${e.localizedMessage}")
    }
}