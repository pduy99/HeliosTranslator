package com.helios.sunverta.android.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
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

@Composable
fun LanguagePickerComponent(
    availableLanguages: List<UiLanguage>,
    fromLanguage: UiLanguage,
    isChoosingFromLanguage: Boolean,
    toLanguage: UiLanguage,
    isChoosingToLanguage: Boolean,
    onOpenFromLanguage: () -> Unit,
    onOpenToLanguage: () -> Unit,
    onStopChoosingLanguage: () -> Unit,
    onSelectToLanguage: (UiLanguage) -> Unit,
    onSelectFromLanguage: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier,
    enableSwapLanguage: Boolean = true,
    onSwapLanguages: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageSelector(
            availableLanguages = availableLanguages,
            language = fromLanguage,
            isOpen = isChoosingFromLanguage,
            onClick = onOpenFromLanguage,
            onDismiss = onStopChoosingLanguage,
            onSelectLanguage = onSelectFromLanguage,
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
                .clickable { if (enableSwapLanguage) onSwapLanguages?.invoke() },
            imageVector = if (enableSwapLanguage) Icons.Outlined.SwapHoriz else Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )

        LanguageSelector(
            availableLanguages = availableLanguages,
            language = toLanguage,
            isOpen = isChoosingToLanguage,
            onClick = onOpenToLanguage,
            onDismiss = onStopChoosingLanguage,
            onSelectLanguage = onSelectToLanguage,
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
            onOpenFromLanguage = { },
            onSwapLanguages = { },
            onStopChoosingLanguage = { },
            onOpenToLanguage = { },
            onSelectFromLanguage = { },
            onSelectToLanguage = { },
            enableSwapLanguage = true,
            availableLanguages = emptyList()
        )
    }

}