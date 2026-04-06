package com.fecrin.eroxia.data.repository

import com.fecrin.eroxia.data.json
import com.fecrin.eroxia.data.remote.MessageRouter
import com.fecrin.eroxia.data.remote.WebSocketService
import com.fecrin.eroxia.data.remote.model.ClientMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ConnectionRepository @Inject constructor(
    private val wss: WebSocketService, val router: MessageRouter
) {

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    fun connect(url: String) {

        wss.connect(url = url, onMessage = { message ->
            router.route(message)
        }, onConnected = {
            _connectionState.value = true
        }, onDisconnected = {
            _connectionState.value = false
        })
    }

    @PublishedApi
    internal fun sendRawString(jsonString: String): Boolean {
        return wss.send(jsonString)
    }

    inline fun <reified T> sendMessage(type: String, payload: T): Boolean {
        val clientMessage = ClientMessage(type = type, payload = payload)

        val jsonString = json.encodeToString(clientMessage)

        return sendRawString(jsonString)
    }

    fun disconnect() {
        wss.disconnect()
        _connectionState.value = false
    }


}