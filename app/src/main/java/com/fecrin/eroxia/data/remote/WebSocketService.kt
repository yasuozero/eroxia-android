package com.fecrin.eroxia.data.remote

import com.fecrin.eroxia.data.json
import com.fecrin.eroxia.data.remote.model.ServerMessage
import kotlinx.serialization.json.Json
import okhttp3.*
import javax.inject.Inject

class WebSocketService @Inject constructor() {

    private val client = OkHttpClient()
    private var ws: WebSocket? = null

    fun connect(
        url: String,
        onMessage: (ServerMessage) -> Unit,
        onConnected: () -> Unit,
        onDisconnected: () -> Unit
    ) {
        val request = Request.Builder().url(url).build()

        ws = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                onConnected()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val message = json.decodeFromString<ServerMessage>(text)
                onMessage(message)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onDisconnected()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                onDisconnected()
            }
        })
    }

    fun send(text: String): Boolean {
        return ws?.send(text) ?: false
    }

    fun disconnect() {
        ws?.close(1000, "ws closed.")
        ws = null
    }
}