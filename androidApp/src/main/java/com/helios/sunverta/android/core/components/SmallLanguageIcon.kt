package com.helios.sunverta.android.core.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun SmallLanguageIcon(language: UiLanguage, modifier: Modifier = Modifier) {
    AsyncImage(
        model = language.drawableRes,
        contentDescription = language.displayNameInEnglish,
        modifier = modifier.size(25.dp)
    )
}