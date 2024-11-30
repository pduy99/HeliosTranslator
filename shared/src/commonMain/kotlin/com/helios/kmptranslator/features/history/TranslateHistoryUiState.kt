package com.helios.kmptranslator.features.history

import com.helios.kmptranslator.core.presentation.UiHistoryItem

data class TranslateHistoryUiState(
    val menuExpanded: Boolean = false,
    val history: List<UiHistoryItem> = emptyList()
)