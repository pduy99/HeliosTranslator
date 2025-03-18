package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language

actual class UiLanguage(
    actual val language: Language,
    val imageName: String
) {

    actual companion object {
        actual fun fromLanguageCode(languageCode: String): UiLanguage {
            val language = Language(langCode = languageCode)
            return fromLanguage(language)
        }

        actual fun fromLanguage(language: Language): UiLanguage {
            return UiLanguage(
                language = language,
                imageName = language.langCode
            )
        }
    }
}