package com.helios.sunverta.android.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    languages: List<UiLanguage>,
    selectedLanguage: UiLanguage,
    title: String,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onLanguageSelected: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        dragHandle = null
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDismissRequest
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Close bottom sheet"
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurface)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    "All languages", style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            items(languages) {
                LanguageItem(
                    language = it,
                    isSelected = it.language.langCode == selectedLanguage.language.langCode,
                    onClick = {
                        onLanguageSelected(it)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: UiLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) else Color.Transparent)
            .padding(10.dp)
    ) {
        Text(
            text = language.displayNameInEnglish!!,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewLanguageSelectionBottomSheet() {
    HeliosTranslatorTheme {
        LanguageSelectionBottomSheet(
            sheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Expanded,
                skipHiddenState = true
            ),
            title = "Translate from",
            languages = listOf(
                UiLanguage.fromLanguageCode("en"),
                UiLanguage.fromLanguageCode("es"),
                UiLanguage.fromLanguageCode("fr"),
                UiLanguage.fromLanguageCode("de"),
                UiLanguage.fromLanguageCode("it"),
                UiLanguage.fromLanguageCode("pt"),
                UiLanguage.fromLanguageCode("zh"),
                UiLanguage.fromLanguageCode("ar"),
                UiLanguage.fromLanguageCode("ru"),
                UiLanguage.fromLanguageCode("ja"),
                UiLanguage.fromLanguageCode("ko")
            ),
            selectedLanguage = UiLanguage.fromLanguageCode("en"),
            onDismissRequest = {},
            onLanguageSelected = {}
        )
    }
}

