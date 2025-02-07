package com.helios.kmptranslator.android.features.scan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TranslatedImageHeader(
    modifier: Modifier = Modifier,
    textVisible: Boolean,
    onClose: () -> Unit,
    onToggleTextVisibility: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onClose
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            onClick = onToggleTextVisibility
        ) {
            Icon(
                imageVector = if (textVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}