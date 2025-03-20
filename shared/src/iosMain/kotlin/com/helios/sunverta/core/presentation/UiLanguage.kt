package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

actual class UiLanguage(
    actual val language: Language,
    val imageName: String
) {

    actual val bcp47Code: String?
        get() {
            return language.langCode
        }
    actual val nativeName: String?
        get() {
            val englishLocale = NSLocale("en_US")
            return englishLocale.localizedStringForLanguageCode(language.langCode)
        }
    actual val displayNameInEnglish: String?
        get() {
            return NSLocale.currentLocale.localizedStringForLanguageCode(language.langCode)
        }

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