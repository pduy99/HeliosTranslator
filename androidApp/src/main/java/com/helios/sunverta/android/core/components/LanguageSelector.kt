package com.helios.sunverta.android.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    language: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    availableLanguages: List<UiLanguage>,
    onSelectLanguage: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onClick() }) {
        Text(
            text = language.displayNameInEnglish!!,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    if (isOpen) {
        LanguageSelectionBottomSheet(
            languages = availableLanguages,
            selectedLanguage = language,
            title = "Select Language",
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = onDismiss,
            onLanguageSelected = onSelectLanguage,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .safeGesturesPadding()
        )
    }
}

@Preview
@Composable
private fun LanguageSelectorPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        LanguageSelector(
            language = UiLanguage.fromLanguageCode("en"),
            isOpen = false,
            onClick = {},
            onDismiss = {},
            availableLanguages = emptyList(),
            onSelectLanguage = {})
    }
}