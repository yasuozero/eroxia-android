package com.fecrin.eroxia.domain

import com.fecrin.eroxia.data.remote.model.AdminAuthPayload
import com.fecrin.eroxia.data.repository.ConnectionRepository
import javax.inject.Inject


class AuthenticateAsAdminUseCase @Inject constructor(private val repository: ConnectionRepository) {

    operator fun invoke(password: String): Boolean {
        return repository.sendMessage<AdminAuthPayload>("admin", AdminAuthPayload(password))
    }

}