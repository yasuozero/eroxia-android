package com.fecrin.eroxia.presentation.screens.history

import androidx.lifecycle.ViewModel
import com.fecrin.eroxia.data.repository.TelemetryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TelemetryRepository
) : ViewModel() {

    val sessions: Flow<List<String>> = repository.getSessions()

}