package com.fecrin.eroxia.presentation.screens.home

sealed class HomeUiState {
    data object Disconnected: HomeUiState()
    data object AdminTaken: HomeUiState()
    data object AdminAvailable: HomeUiState()
}