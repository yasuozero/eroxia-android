package com.fecrin.eroxia.presentation.screens.control.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


data class ControlButtonData(
    val icon: ImageVector,
    val contentDescription: String,
    val backgroundColor: Color,
    val iconColor: Color = Color.White,
    val onClick: () -> Unit
)