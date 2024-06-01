package com.helios.kmptranslator.translate

import androidx.compose.runtime.Stable
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.core.presentation.UiHistoryItem

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("de"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList()
)