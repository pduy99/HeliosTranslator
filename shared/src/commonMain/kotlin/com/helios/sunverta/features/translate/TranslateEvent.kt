package com.helios.sunverta.features.translate

import com.helios.sunverta.core.presentation.UiLanguage

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

    data object EditTranslation : TranslateEvent()

    data object OnErrorSeen : TranslateEvent()

    data object ReadAloudText : TranslateEvent()

    data object OpenHistoryScreen : TranslateEvent()

    data object OpenCameraTranslateScreen : TranslateEvent()

    data object OpenConversationTranslateScreen : TranslateEvent()
}