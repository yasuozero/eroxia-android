package com.fecrin.eroxia.data.repository

import android.util.Log
import com.fecrin.eroxia.data.json
import com.fecrin.eroxia.data.local.dao.TelemetryDao
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

@Singleton
class ConnectionRepository @Inject constructor(
    private val wss: WebSocketService,
    val router: MessageRouter,
    private val telemetryDao: TelemetryDao
) {
    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState.asStateFlow()

    private var reconnectJob: Job? = null
    private var currentUrl: String? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var currentSessionId: String? = null

    init {
        scope.launch {
            router.handshake.collect { payload ->
                currentSessionId = payload.sessionId
                Log.d("EroxiaDB", "Yeni oturum başladı: $currentSessionId")
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
                    telemetryDao.insertTelemetry(entity)
                    Log.d("EroxiaDB", "Veri Kaydedildi! Zaman: ${entity.t}")
                } ?: Log.e("EroxiaDB", "Veri geldi ama Session ID yok! Kaydedilmedi.")
            }
        }
    }

    fun connect(url: String) {
        currentUrl = url
        reconnectJob?.cancel()

        reconnectJob = scope.launch {
            while (isActive) {
                if (!_connectionState.value) {
                    establishConnection(url)
                }
                delay(3000)
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
                _connectionState.value = true
            },
            onDisconnected = {
                currentSessionId = null
                _connectionState.value = false
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

    fun disconnect() {
        reconnectJob?.cancel()
        currentUrl = null
        currentSessionId = null
        wss.disconnect()
        _connectionState.value = false
    }
}