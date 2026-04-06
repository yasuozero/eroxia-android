package com.fecrin.eroxia.presentation.screens.control.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fecrin.eroxia.presentation.screens.control.models.ControlButtonData


@Composable
fun GridIconButton(
    data: ControlButtonData,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = data.onClick,
        shape = RoundedCornerShape(16.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = data.backgroundColor,
            contentColor = data.iconColor
        ),
        modifier = modifier.aspectRatio(1f)
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.contentDescription,
            modifier = Modifier.size(48.dp)
        )
    }
}


@Composable
fun ControlPadGrid(
    buttons: List<ControlButtonData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        buttons.chunked(3).forEach { rowButtons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowButtons.forEach { buttonData ->
                    GridIconButton(
                        data = buttonData,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}