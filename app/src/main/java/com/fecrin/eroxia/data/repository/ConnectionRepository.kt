package com.fecrin.eroxia.data.repository

import com.fecrin.eroxia.data.json
import com.fecrin.eroxia.data.local.entity.TelemetryEntity
import com.fecrin.eroxia.data.remote.MessageRouter
import com.fecrin.eroxia.data.remote.WebSocketService
import com.fecrin.eroxia.data.remote.model.ClientMessage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

enum class ConnectionStatus { DISCONNECTED, CONNECTING, CONNECTED }

@Singleton
class ConnectionRepository @Inject constructor(
    private val wss: WebSocketService,
    val router: MessageRouter,
    private val telemetryRepository: TelemetryRepository
) {
    private val _connectionState = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    val connectionState: StateFlow<ConnectionStatus> = _connectionState.asStateFlow()

    private var reconnectJob: Job? = null
    private var currentUrl: String? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Volatile
    private var currentSessionId: String? = null

    init {
        scope.launch {
            router.handshake.collect { payload ->
                currentSessionId = payload.sessionId
            }
        }

        scope.launch {
            router.telemetry.collect { payload ->
                currentSessionId?.let { sessionId ->
                    val entity = TelemetryEntity(
                        sessionId = sessionId,
                        t = payload.t,
                        motion = payload.motion,
                        process = payload.process
                    )
                    telemetryRepository.saveTelemetry(entity)
                }
            }
        }
    }

    fun connect(url: String) {
        currentUrl = url
        reconnectJob?.cancel()

        reconnectJob = scope.launch {
            while (isActive) {
                if (_connectionState.value == ConnectionStatus.DISCONNECTED) {
                    _connectionState.value = ConnectionStatus.CONNECTING

                    wss.disconnect()
                    establishConnection(url)
                }

                if (_connectionState.value == ConnectionStatus.CONNECTED) {
                    _connectionState.first { it == ConnectionStatus.DISCONNECTED }
                } else {
                    delay(3000)
                }
            }
        }
    }

    private fun establishConnection(url: String) {
        wss.connect(
            url = url,
            onMessage = { message ->
                router.route(message)
            },
            onConnected = {
                _connectionState.value = ConnectionStatus.CONNECTED
            },
            onDisconnected = {
                currentSessionId = null
                _connectionState.value = ConnectionStatus.DISCONNECTED
            }
        )
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

    @Suppress("unused")
    fun disconnect() {
        reconnectJob?.cancel()
        currentUrl = null
        currentSessionId = null
        wss.disconnect()
        _connectionState.value = ConnectionStatus.DISCONNECTED
    }
}