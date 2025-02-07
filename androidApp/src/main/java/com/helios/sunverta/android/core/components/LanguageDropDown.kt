package com.helios.sunverta.android.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.helios.sunverta.android.core.theme.HeliosTranslatorTheme
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun LanguageDropDown(
    language: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectLanguage: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLanguages = remember { UiLanguage.allLanguages }
    Box(modifier = modifier.clickable { onClick() }) {
        Text(
            text = language.language.langName,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onPrimary
        )
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = onDismiss,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            allLanguages.forEach { language ->
                LanguageDropDownItem(
                    language = language,
                    onClick = { onSelectLanguage(language) }
                )
            }
        }
    }

}

@Preview
@Composable
fun LanguageDropDownPreview() {
    HeliosTranslatorTheme(darkTheme = true) {
        LanguageDropDown(
            language = UiLanguage.byCode("en"),
            isOpen = false,
            onClick = {},
            onDismiss = {},
            onSelectLanguage = {})
    }
}