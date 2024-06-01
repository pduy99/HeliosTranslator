package com.helios.kmptranslator.translate

import com.helios.kmptranslator.core.presentation.UiHistoryItem
import com.helios.kmptranslator.core.presentation.UiLanguage

sealed class TranslateEvent {

    data class ChooseFromLanguage(val language: UiLanguage) : TranslateEvent()

    data class ChooseToLanguage(val language: UiLanguage) : TranslateEvent()

    data object StopChoosingLanguage : TranslateEvent()

    data object SwapLanguages : TranslateEvent()

    data class ChangeTranslationText(val text: String) : TranslateEvent()

    data object Translate : TranslateEvent()

    data object OpenFromLanguageDropDown : TranslateEvent()

    data object OpenToLanguageDropDown : TranslateEvent()

    data object CloseTranslation : TranslateEvent()

    data class SubmitVoiceResult(val voiceResult: String?) : TranslateEvent()

    data class SelectHistoryTranslationItem(val item: UiHistoryItem) : TranslateEvent()

    data object EditTranslation : TranslateEvent()

    data object OnErrorSeen : TranslateEvent()
}