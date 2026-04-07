package com.fecrin.eroxia.presentation.screens.control

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fecrin.eroxia.R
import com.fecrin.eroxia.presentation.screens.control.components.ControlPadGrid
import com.fecrin.eroxia.presentation.screens.control.components.ModsButton
import com.fecrin.eroxia.presentation.screens.control.components.ProcessCard
import com.fecrin.eroxia.presentation.screens.control.models.ControlButtonData
import com.fecrin.eroxia.presentation.screens.control.models.ControlUiState

@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ControlScreenContent(
        uiState = uiState, onPowerClick = { viewModel.togglePower() })
}

@Composable
private fun ControlScreenContent(
    uiState: ControlUiState, onPowerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Crossfade(
            targetState = uiState.isRunning,
            label = "PowerStateAnimation",
            modifier = Modifier.weight(1f)
        ) { isRunning ->

            if (isRunning) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    ControlProcessArea(
                        temperature = uiState.temperature,
                        pressure = uiState.pressure,
                        speed = uiState.speed
                    )

                    ModsButton {}

                    ControlPad(
                        onPowerClick = onPowerClick
                    )
                }
            } else {

                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = onPowerClick,
                        modifier = Modifier.size(120.dp),
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Start System",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlProcessArea(temperature: Int, pressure: Int, speed: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProcessCard(
            title = stringResource(id = R.string.process_temperature),
            unit = "°C",
            value = temperature,
            modifier = Modifier.weight(1f)
        )
        ProcessCard(
            title = stringResource(id = R.string.process_pressure),
            unit = "hPa",
            value = pressure,
            modifier = Modifier.weight(1f)
        )
        ProcessCard(
            title = stringResource(id = R.string.process_speed),
            unit = "m/s",
            value = speed,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ControlPad(
    onPowerClick: () -> Unit, viewModel: ControlViewModel = hiltViewModel()
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val buttonList = listOf(
        ControlButtonData(
            Icons.AutoMirrored.Default.RotateLeft,
            "Rotate Left",
            secondaryColor
        ) {
            viewModel.sendCommand("ROTATE_LEFT")
        },
        ControlButtonData(Icons.Default.KeyboardArrowUp, "Up", primaryColor) {
            viewModel.sendCommand("UP")
        },
        ControlButtonData(Icons.AutoMirrored.Default.CallMade, "Top Right", secondaryColor) {
            viewModel.sendCommand("TOP_RIGHT")
        },

        ControlButtonData(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Left", primaryColor) {
            viewModel.sendCommand("LEFT")
        },
        ControlButtonData(Icons.Default.Pause, "Power", Color(0xFFF44336)) {
            onPowerClick()
        },
        ControlButtonData(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Right", primaryColor) {
            viewModel.sendCommand("RIGHT")
        },

        ControlButtonData(Icons.AutoMirrored.Default.CallReceived, "Bottom Left", secondaryColor) {
            viewModel.sendCommand("BOTTOM_LEFT")
        },
        ControlButtonData(Icons.Default.KeyboardArrowDown, "Down", primaryColor) {
            viewModel.sendCommand("DOWN")
        },
        ControlButtonData(Icons.AutoMirrored.Filled.RotateRight, "Rotate Right", secondaryColor) {
            viewModel.sendCommand("ROTATE_RIGHT")
        })

    Box(modifier = Modifier.padding(24.dp)) {
        ControlPadGrid(buttons = buttonList)
    }
}

@Preview(showBackground = true)
@Composable
private fun ControlScreenPreview() {
    ControlScreenContent(
        uiState = ControlUiState(
            isRunning = true, temperature = 25, pressure = 1013, speed = 10
        ), onPowerClick = {})
}