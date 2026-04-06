package com.fecrin.eroxia.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fecrin.eroxia.R
import com.fecrin.eroxia.presentation.screens.home.HomeUiState

@Composable
fun HomeStateView(
    title: String,
    description: String,
    icon: ImageVector,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    state: HomeUiState = HomeUiState.Disconnected,
    onJoinAdminClick: () -> Unit = {},
    onJoinViewerClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(

            imageVector = icon, contentDescription = null, modifier = Modifier.size(128.dp), iconTint
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(0.85F)
        )

        Spacer(modifier = Modifier.height(14.dp))

        when (state) {
            HomeUiState.AdminAvailable -> {
                JoinButton(text = stringResource(id = R.string.join_admin), onClick = onJoinAdminClick)
                Spacer(modifier = Modifier.height(8.dp))
                JoinButton(text = stringResource(id = R.string.join_viewer),onClick = onJoinViewerClick)
            }

            HomeUiState.AdminTaken -> {
                JoinButton(text = stringResource(id = R.string.join_viewer),onClick = onJoinViewerClick)
            }

            else -> Unit
        }
    }
}

@Composable
private fun JoinButton(text: String, onClick: () -> Unit = {}) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth(0.75f)) {
        Text(text)
    }
}
