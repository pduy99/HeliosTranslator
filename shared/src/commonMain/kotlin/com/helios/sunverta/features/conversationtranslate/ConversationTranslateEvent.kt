package com.helios.sunverta.features.conversationtranslate

import com.helios.sunverta.core.presentation.UiLanguage

sealed class ConversationTranslateEvent {

    data class PermissionResult(val isGranted: Boolean) : ConversationTranslateEvent()

    data class OpenLanguageDropDown(val person: ConversationTranslateUiState.TalkingPerson) :
        ConversationTranslateEvent()

    data class ChooseLanguage(
        val person: ConversationTranslateUiState.TalkingPerson,
        val language: UiLanguage
    ) : ConversationTranslateEvent()

    data object StopChoosingLanguage : ConversationTranslateEvent()

    data class ToggleRecording(val person: ConversationTranslateUiState.TalkingPerson) :
        ConversationTranslateEvent()

    data object ToggleFaceToFaceMode : ConversationTranslateEvent()
}