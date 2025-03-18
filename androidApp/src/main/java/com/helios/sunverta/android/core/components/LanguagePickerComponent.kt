package com.helios.sunverta.android.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.translate.TranslateEvent

@Composable
fun LanguagePickerComponent(
    availableLanguages: List<UiLanguage>,
    fromLanguage: UiLanguage,
    isChoosingFromLanguage: Boolean,
    toLanguage: UiLanguage,
    isChoosingToLanguage: Boolean,
    onEvent: (TranslateEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageDropDown(
            availableLanguages = availableLanguages,
            language = fromLanguage,
            isOpen = isChoosingFromLanguage,
            onClick = { onEvent(TranslateEvent.OpenFromLanguageDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = { onEvent(TranslateEvent.ChooseFromLanguage(it)) },
            modifier = Modifier
                .weight(3.5f)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.primary)
                .aspectRatio(3f)
        )

        Icon(
            modifier = Modifier
                .weight(1f)
                .size(32.dp)
                .clickable { onEvent(TranslateEvent.SwapLanguages) },
            imageVector = Icons.Outlined.SwapHoriz,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        LanguageDropDown(
            availableLanguages = availableLanguages,
            language = toLanguage,
            isOpen = isChoosingToLanguage,
            onClick = { onEvent(TranslateEvent.OpenToLanguageDropDown) },
            onDismiss = { onEvent(TranslateEvent.StopChoosingLanguage) },
            onSelectLanguage = { onEvent(TranslateEvent.ChooseToLanguage(it)) },
            modifier = Modifier
                .weight(3.5f)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colorScheme.primary)
                .aspectRatio(3f)
        )
    }
}

@Preview
@Composable
fun LanguagePickerComponentPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        LanguagePickerComponent(
            fromLanguage = UiLanguage.fromLanguageCode("en"),
            isChoosingFromLanguage = false,
            toLanguage = UiLanguage.fromLanguageCode("ja"),
            isChoosingToLanguage = false,
            onEvent = {},
            availableLanguages = emptyList()
        )
    }

}