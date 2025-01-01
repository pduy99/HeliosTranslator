package com.helios.kmptranslator.android.features.text.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.components.SmallLanguageIcon
import com.helios.kmptranslator.core.presentation.UiLanguage

@Composable
fun LanguageDisplay(language: UiLanguage, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        SmallLanguageIcon(language = language)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = language.language.langName, color = MaterialTheme.colorScheme.onSurface)
    }
}