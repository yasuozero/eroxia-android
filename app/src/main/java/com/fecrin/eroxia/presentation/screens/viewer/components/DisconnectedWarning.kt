package com.fecrin.eroxia.presentation.screens.viewer.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DisconnectedWarning(modifier: Modifier = Modifier) {
    androidx.compose.material3.Surface(
        modifier = modifier.padding(top = 16.dp),
        color = Color.Red.copy(alpha = 0.8f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "Bağlantı Koptu. Yeniden bağlanılıyor...",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}