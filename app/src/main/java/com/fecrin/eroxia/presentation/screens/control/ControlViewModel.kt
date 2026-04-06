package com.fecrin.eroxia.presentation.screens.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fecrin.eroxia.data.repository.ConnectionRepository
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
    private val repository: ConnectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ControlUiState())

    val uiState: StateFlow<ControlUiState> = _uiState.asStateFlow()

//    init {
//        listenProcess()
//    }

    fun togglePower() {
        _uiState.update { currentState ->
            currentState.copy(
                isRunning = !currentState.isRunning
            )
        }
    }

//
//    private fun listenProcess() {
//        viewModelScope.launch {
//            repository.router.process.collect { payload ->
//                updateMetrics(payload.temperature, payload.pressure, payload.speed)
//            }
//        }
//    }

    private fun updateMetrics(newTemperature: Int, newPressure: Int, newSpeed: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                temperature = newTemperature, pressure = newPressure, speed = newSpeed
            )
        }
    }
}