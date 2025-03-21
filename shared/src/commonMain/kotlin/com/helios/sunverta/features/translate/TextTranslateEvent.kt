package com.helios.sunverta.features.translate

import com.helios.sunverta.core.presentation.UiLanguage

sealed class TextTranslateEvent {

    data class ChooseFromLanguage(val language: UiLanguage) : TextTranslateEvent()

    data class ChooseToLanguage(val language: UiLanguage) : TextTranslateEvent()

    data object StopChoosingLanguage : TextTranslateEvent()

    data object SwapLanguages : TextTranslateEvent()

    data class ChangeTranslationText(val text: String) : TextTranslateEvent()

    data object TextTranslate : TextTranslateEvent()

    data object OpenFromLanguageDropDown : TextTranslateEvent()

    data object OpenToLanguageDropDown : TextTranslateEvent()

    data object CloseTranslation : TextTranslateEvent()

    data object EditTranslation : TextTranslateEvent()

    data object OnErrorSeen : TextTranslateEvent()

    data object ReadAloudText : TextTranslateEvent()

    data object OpenHistoryScreen : TextTranslateEvent()

    data object OpenCameraTextTranslateScreen : TextTranslateEvent()

    data object OpenConversationTextTranslateScreen : TextTranslateEvent()
}