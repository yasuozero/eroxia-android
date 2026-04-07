package com.fecrin.eroxia.presentation.screens.viewer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fecrin.eroxia.presentation.screens.viewer.components.DisconnectedWarning
import com.fecrin.eroxia.presentation.screens.viewer.components.EmptyTelemetryState
import com.fecrin.eroxia.presentation.screens.viewer.components.TelemetryChart
import com.fecrin.eroxia.ui.components.LockScreenOrientation

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun ViewerScreen(
    viewModel: ViewerViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val points by viewModel.points.collectAsStateWithLifecycle()
    val isConnected by viewModel.connectionState.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()) {
        if (points.isEmpty()) {
            EmptyTelemetryState()
        } else {
            TelemetryChart(viewModel)
        }

        if (!isConnected) {
            DisconnectedWarning(modifier = Modifier.align(Alignment.TopCenter))
        }
    }
}