package com.helios.kmptranslator.android.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.R
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme

@Composable
fun SwapLanguagesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        Icon(
            imageVector = Icons.Filled.Repeat,
            contentDescription = stringResource(
                id = R.string.swap_languages
            ),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun SwapLanguagesButtonPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        SwapLanguagesButton(onClick = {})
    }
}