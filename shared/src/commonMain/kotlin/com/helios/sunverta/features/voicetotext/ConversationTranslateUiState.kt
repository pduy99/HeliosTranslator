package com.helios.sunverta.features.voicetotext

import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.features.translate.TranslateError

data class ConversationTranslateUiState(
    val personOne: PersonState = PersonState(language = UiLanguage.byCode("en")),
    val personTwo: PersonState = PersonState(language = UiLanguage.byCode("de")),
    val powerRatio: Float = 0f,
    val faceToFaceMode: Boolean = false,
    val personTalking: TalkingPerson = TalkingPerson.NONE,
    val isTranslating: Boolean = false,
    val isListening: Boolean = false,
    val canRecord: Boolean = false,
    val recordError: String? = null,
    val error: TranslateError? = null
) {
    data class PersonState(
        val language: UiLanguage,
        val isChoosingLanguage: Boolean = false,
        val text: String = ""
    )

    enum class TalkingPerson {
        NONE,
        PERSON_ONE,
        PERSON_TWO
    }

    fun getCurrentLanguage(person: TalkingPerson): UiLanguage {
        return when (person) {
            TalkingPerson.PERSON_ONE -> personOne.language
            TalkingPerson.PERSON_TWO -> personTwo.language
            TalkingPerson.NONE -> throw IllegalArgumentException("Cannot get language for NONE person")
        }
    }
}