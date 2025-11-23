package com.helios.sunverta.android.core.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun SmallLanguageIcon(language: UiLanguage, modifier: Modifier = Modifier) {
    Text(
        text = language.flagEmoji,
        fontSize = 25.sp,
        modifier = modifier
    )
}