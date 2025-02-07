package com.helios.sunverta.features.translate

import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.presentation.UiHistoryItem
import com.helios.sunverta.core.presentation.UiLanguage

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