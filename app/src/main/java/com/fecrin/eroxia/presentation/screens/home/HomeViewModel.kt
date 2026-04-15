package com.fecrin.eroxia.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fecrin.eroxia.R
import com.fecrin.eroxia.data.remote.Constants
import com.fecrin.eroxia.data.repository.ConnectionRepository
import com.fecrin.eroxia.domain.AuthenticateAsAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ConnectionRepository,private val authenticateAsAdmin: AuthenticateAsAdminUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Disconnected)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _adminError = MutableStateFlow<Int?>(null)
    val adminError: StateFlow<Int?> = _adminError.asStateFlow()

    private val _adminSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val adminSuccess: SharedFlow<Unit> = _adminSuccess.asSharedFlow()

    init {
        connect()
        listenHandshake()
        listenConnection()
        listenAdminAuth()
    }

    private fun connect() {
        repository.connect(Constants.WS_URL)
    }

    private fun listenHandshake() {
        viewModelScope.launch {
            repository.router.handshake.collect { payload ->
                _uiState.value = when {
                    payload.admin -> HomeUiState.AdminTaken
                    else -> HomeUiState.AdminAvailable
                }
            }
        }
    }

    private fun listenAdminAuth() {
        viewModelScope.launch {
            repository.router.admin.collect { payload ->
                when (payload.status) {
                    "failed" -> {
                        _adminError.value = R.string.error_incorrect_password
                    }
                    "success" -> {
                        _adminError.value = null
                        _adminSuccess.tryEmit(Unit)
                    }
                    "occupied" -> {
                        _adminError.value = R.string.error_session_full
                    }
                    "available" -> {
                        _adminError.value = null
                    }
                }
            }
        }
    }

    private fun listenConnection() {
        viewModelScope.launch {
            repository.connectionState.collect { connected ->
                if (!connected) {
                    _uiState.value = HomeUiState.Disconnected
                }
            }
        }
    }

    fun loginAsAdmin(password: String) {
        authenticateAsAdmin(password)
    }
}