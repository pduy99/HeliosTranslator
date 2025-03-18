package com.helios.sunverta.android.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helios.sunverta.core.presentation.UiLanguage

@Composable
fun LanguageDropDownItem(
    language: UiLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    DropdownMenuItem(
        modifier = modifier,
        text = { Text(text = language.language.displayNameInEnglish!!) },
        onClick = onClick,
        leadingIcon = {
            Image(
                painter = painterResource(id = language.drawableRes),
                contentDescription = language.language.displayNameInEnglish,
                modifier = Modifier.size(40.dp)
            )
        }
    )
}

@Preview
@Composable
fun LanguageDropDownItemPreview() {
    LanguageDropDownItem(
        language = UiLanguage.fromLanguageCode("en"),
        onClick = {},
        modifier = Modifier
    )
}