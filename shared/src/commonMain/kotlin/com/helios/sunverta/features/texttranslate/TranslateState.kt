package com.helios.sunverta.features.texttranslate

import androidx.compose.runtime.Immutable
import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.presentation.UiLanguage

@Immutable
data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.fromLanguageCode("en"),
    val toLanguage: UiLanguage = UiLanguage.fromLanguageCode("es"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslateError? = null,
    val availableLanguages: List<UiLanguage> = emptyList(),
)