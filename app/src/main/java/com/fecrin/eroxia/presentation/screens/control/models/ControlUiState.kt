package com.fecrin.eroxia.presentation.screens.control.models

data class ControlUiState(
    val isRunning: Boolean = false,
    val temperature: Int = 0,
    val pressure: Int = 0,
    val speed: Int = 0,
    val isLoading: Boolean = false
)