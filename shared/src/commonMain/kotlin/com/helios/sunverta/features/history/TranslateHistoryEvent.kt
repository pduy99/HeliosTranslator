package com.helios.sunverta.features.history

sealed class TranslateHistoryEvent {
    data class ToggleMenu(val forceClose: Boolean = false) : TranslateHistoryEvent()
    data object DeleteAllHistory : TranslateHistoryEvent()
}