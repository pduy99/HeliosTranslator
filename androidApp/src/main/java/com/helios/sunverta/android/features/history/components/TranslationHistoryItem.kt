package com.helios.sunverta.android.features.history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.components.SmallLanguageIcon
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiHistoryItem
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun TranslationHistoryItem(
    historyItem: UiHistoryItem,
    onCopyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 5.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LanguageSection(
                language = historyItem.fromLanguage,
                text = historyItem.fromText,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onCopyClick = onCopyClick
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)

            LanguageSection(
                language = historyItem.toLanguage,
                text = historyItem.toText,
                textColor = MaterialTheme.colorScheme.inversePrimary,
                textStyle = MaterialTheme.typography.bodyLarge,
                onCopyClick = onCopyClick
            )
        }
    }
}

@Composable
private fun LanguageSection(
    language: UiLanguage,
    text: String,
    textColor: Color,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onCopyClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallLanguageIcon(language = language)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = language.displayNameInEnglish ?: "Unknown",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                modifier = Modifier.size(32.dp),
                onClick = {
                    onCopyClick(text)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            style = textStyle,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun TranslationHistoryItemPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        TranslationHistoryItem(
            historyItem = UiHistoryItem(
                fromLanguage = UiLanguage.fromLanguageCode("en"),
                toLanguage = UiLanguage.fromLanguageCode("ja"),
                fromText = "Hello, world!",
                toText = "¡Hola, mundo!",
                id = 1
            ),
            onCopyClick = {}
        )
    }
}