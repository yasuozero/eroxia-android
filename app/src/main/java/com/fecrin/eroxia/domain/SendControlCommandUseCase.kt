package com.fecrin.eroxia.domain

import com.fecrin.eroxia.data.remote.model.CommandPayload
import com.fecrin.eroxia.data.repository.ConnectionRepository
import javax.inject.Inject

class SendControlCommandUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {
    operator fun invoke(action: String): Boolean {
        return repository.sendMessage(
            type = "command",
            payload = CommandPayload(action = action)
        )
    }
}