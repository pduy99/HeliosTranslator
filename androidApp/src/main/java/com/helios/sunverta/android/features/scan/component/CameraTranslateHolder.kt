package com.helios.sunverta.android.features.scan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.components.LanguagePickerComponent
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.scantranslate.ScanTranslateEvent

@Composable
fun CameraTranslateHolder(
    modifier: Modifier = Modifier,
    fromLanguage: UiLanguage,
    toLanguage: UiLanguage,
    isChoosingFromLanguage: Boolean,
    isChoosingToLanguage: Boolean,
    availableLanguages: List<UiLanguage>,
    onEvent: (ScanTranslateEvent) -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        content()
        LanguagePickerComponent(
            fromLanguage = fromLanguage,
            isChoosingFromLanguage = isChoosingFromLanguage,
            toLanguage = toLanguage,
            isChoosingToLanguage = isChoosingToLanguage,
            onOpenFromLanguage = { onEvent(ScanTranslateEvent.OpenFromLanguagePicker) },
            onStopChoosingLanguage = { onEvent(ScanTranslateEvent.StopChoosingLanguage) },
            onOpenToLanguage = { onEvent(ScanTranslateEvent.OpenToLanguagePicker) },
            onSelectFromLanguage = { onEvent(ScanTranslateEvent.ChooseFromLanguage(it)) },
            onSelectToLanguage = { onEvent(ScanTranslateEvent.ChooseToLanguage(it)) },
            availableLanguages = availableLanguages,
            enableSwapLanguage = false,
            modifier = Modifier
                .fillMaxWidth()
                .safeContentPadding()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
private fun CameraTranslateHolderPreview() {
    HeliosTranslatorTheme {
        CameraTranslateHolder(
            fromLanguage = UiLanguage.fromLanguageCode("en"),
            toLanguage = UiLanguage.fromLanguageCode("ja"),
            onEvent = {},
            availableLanguages = emptyList(),
            isChoosingFromLanguage = false,
            isChoosingToLanguage = false,
            content = {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Gray)
                )
            }
        )
    }
}