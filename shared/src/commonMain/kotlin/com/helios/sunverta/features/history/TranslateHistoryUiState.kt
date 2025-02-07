package com.helios.sunverta.features.history

import com.helios.sunverta.core.presentation.UiHistoryItem

data class TranslateHistoryUiState(
    val menuExpanded: Boolean = false,
    val history: List<UiHistoryItem> = emptyList()
)