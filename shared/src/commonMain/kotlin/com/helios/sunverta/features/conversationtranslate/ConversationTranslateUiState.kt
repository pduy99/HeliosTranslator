package com.helios.sunverta.features.conversationtranslate

import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.presentation.UiLanguage

data class ConversationTranslateUiState(
    val personOne: PersonState = PersonState(UiLanguage.fromLanguageCode("en")),
    val personTwo: PersonState = PersonState(UiLanguage.fromLanguageCode("es")),
    val powerRatio: Float = 0f,
    val faceToFaceMode: Boolean = false,
    val personTalking: TalkingPerson = TalkingPerson.NONE,
    val lastPersonTalking: TalkingPerson = TalkingPerson.NONE,
    val isTranslating: Boolean = false,
    val isListening: Boolean = false,
    val canRecord: Boolean = false,
    val recordError: String? = null,
    val error: TranslateError? = null,
    val availableLanguages: List<UiLanguage> = emptyList()
) {
    data class PersonState(
        val language: UiLanguage,
        val isChoosingLanguage: Boolean = false,
        val text: String = "",
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

    fun isWaitingForTranslation(person: TalkingPerson): Boolean {
        return isTranslating && when (lastPersonTalking) {
            TalkingPerson.PERSON_ONE -> person == TalkingPerson.PERSON_TWO
            TalkingPerson.PERSON_TWO -> person == TalkingPerson.PERSON_ONE
            TalkingPerson.NONE -> false
        }
    }
}