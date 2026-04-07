package com.fecrin.eroxia.presentation.screens.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fecrin.eroxia.data.repository.ConnectionRepository
import com.fecrin.eroxia.domain.SendControlCommandUseCase
import com.fecrin.eroxia.presentation.screens.control.models.ControlUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val repository: ConnectionRepository,
    private val sendControlCommand: SendControlCommandUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ControlUiState())

    val uiState: StateFlow<ControlUiState> = _uiState.asStateFlow()

    init {
        listenTelemetry()
    }


    private fun listenTelemetry() {
        viewModelScope.launch {
            repository.router.telemetry.collect { payload ->
                updateMetrics(
                    newTemperature = payload.process.temperature.toInt(),
                    newPressure = payload.process.pressure.toInt(),
                    newSpeed = payload.process.flow.toInt()
                )
            }
        }
    }

    fun togglePower() {
        _uiState.update { currentState ->
            val newState = !currentState.isRunning

            val powerCommand = if (newState) "POWER_ON" else "POWER_OFF"
            sendCommand(powerCommand)

            currentState.copy(isRunning = newState)

        }
    }

     fun sendCommand(action: String) {
        sendControlCommand(action)
    }

    private fun updateMetrics(newTemperature: Int, newPressure: Int, newSpeed: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                temperature = newTemperature, pressure = newPressure, speed = newSpeed
            )
        }
    }
}