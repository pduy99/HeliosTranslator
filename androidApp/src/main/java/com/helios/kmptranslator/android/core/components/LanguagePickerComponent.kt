package com.helios.kmptranslator.android.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.kmptranslator.android.core.theme.HeliosTranslatorTheme
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.translate.TranslateEvent

@Composable
fun LanguagePickerComponent(
    fromLanguage: UiLanguage,
    isChoosingFromLanguage: Boolean,
    toLanguage: UiLanguage,
    isChoosingToLanguage: Boolean,
    onEvent: (TranslateEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LanguageDropDown(
            language = fromLanguage,
            isOpen = isChoosingFromLanguage,
            onClick = { onEvent(TranslateEvent.OpenFromLanguageDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = {
                onEvent(TranslateEvent.ChooseFromLanguage(it))
            }
        )
        Spacer(modifier = Modifier.weight(1f))
        SwapLanguagesButton(
            onClick = { onEvent(TranslateEvent.SwapLanguages) },
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.weight(1f))
        LanguageDropDown(
            language = toLanguage,
            isOpen = isChoosingToLanguage,
            onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = {
                onEvent(TranslateEvent.ChooseToLanguage(it))
            }
        )
    }
}

@Preview
@Composable
fun LanguagePickerComponentPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        LanguagePickerComponent(
            fromLanguage = UiLanguage.byCode("en"),
            isChoosingFromLanguage = false,
            toLanguage = UiLanguage.byCode("ja"),
            isChoosingToLanguage = false,
            onEvent = {},
        )
    }

}