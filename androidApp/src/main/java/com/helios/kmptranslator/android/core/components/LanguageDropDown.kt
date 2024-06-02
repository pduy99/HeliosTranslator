package com.helios.kmptranslator.android.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.helios.kmptranslator.core.presentation.UiLanguage

@Composable
fun LanguageDropDown(
    language: UiLanguage,
    isOpen: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelectLanguage: (UiLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLanguages = remember {
        UiLanguage.allLanguages
    }
    Box(modifier = modifier) {
        if (isOpen) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = onDismiss
            ) {
                allLanguages.forEach { language ->
                    LanguageDropDownItem(
                        language = language,
                        onClick = { onSelectLanguage(language) },
                        modifier = modifier
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = language.drawableRes,
                contentDescription = language.language.langName,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = language.language.langName, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}