package com.fecrin.eroxia.presentation.screens.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fecrin.eroxia.data.remote.model.TelemetryPayload
import com.fecrin.eroxia.data.repository.ConnectionRepository
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel @Inject constructor(
    private val repository: ConnectionRepository
) : ViewModel() {

    val modelProducer = CartesianChartModelProducer()
    private val maxPts = 30
    private val _points = MutableStateFlow<List<TelemetryPayload>>(emptyList())
    val points = _points.asStateFlow()
    val connectionState = repository.connectionState

    init {
        listenTelemetry()
    }

    private fun listenTelemetry() {
        viewModelScope.launch {
            repository.router.telemetry.collect { payload ->
                _points.update { (it + payload).takeLast(maxPts) }
                launch { syncChart() }
            }
        }
    }

    private suspend fun syncChart() {
        val snap = _points.value.ifEmpty { return }

        modelProducer.runTransaction {
            lineSeries {
                series(snap.map { it.process.pressure })
                series(snap.map { it.process.temperature })
                series(snap.map { it.process.flow })
            }
        }
    }
}