package com.helios.sunverta.android.features.scan.component

import androidx.camera.core.TorchState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraFunctionButtons(
    modifier: Modifier = Modifier,
    torchState: Int,
    onCaptureImageButtonClick: () -> Unit,
    onOpenGalleryClick: () -> Unit,
    onFlashButtonClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onOpenGalleryClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        CaptureImageIcon(
            modifier = Modifier.size(65.dp),
            color = Color.White,
            onClick = onCaptureImageButtonClick
        )

        IconButton(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onFlashButtonClick
        ) {
            Icon(
                imageVector = if (torchState == TorchState.ON) Icons.Outlined.FlashOff else Icons.Outlined.FlashOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}