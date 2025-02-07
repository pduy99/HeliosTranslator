package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language

actual class UiLanguage(
    actual val language: Language,
    val imageName: String
) {

    actual companion object {
        actual fun byCode(languageCode: String): UiLanguage {
            return allLanguages.find { it.language.langCode == languageCode }
                ?: throw IllegalArgumentException("Unsupported language code")
        }

        actual val allLanguages: List<UiLanguage>
            get() = Language.entries.map { language ->
                UiLanguage(language = language, imageName = language.langName.lowercase())
            }
    }

}