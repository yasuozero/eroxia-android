package com.fecrin.eroxia.data.remote

object Constants {
    const val WS_URL = "ws://10.0.2.2:8080"
}

object ServerMessageTypes {
    const val HANDSHAKE = "handshake"
    const val ADMIN_RESULT = "admin_result"
    const val TELEMETRY = "telemetry"
    const val ACTION_RESULT = "action_result"
}