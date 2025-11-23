package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language

expect class UiLanguage {

    val language: Language
    val bcp47Code: String?
    val nativeName: String?
    val displayNameInEnglish: String
    val flagEmoji: String

    companion object {
        fun fromLanguage(language: Language): UiLanguage

        fun fromLanguageCode(languageCode: String): UiLanguage
    }
}