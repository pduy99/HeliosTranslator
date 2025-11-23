package com.helios.sunverta.core.presentation

import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.presentation.LanguageUtils
import java.util.Locale

actual class UiLanguage(
    actual val language: Language,
) {
    actual val bcp47Code: String?
        get() {
            val locales = Locale.getAvailableLocales().filter { it.language == language.langCode }
            val bestLocale =
                locales.firstOrNull { it.country.isNotEmpty() } ?: locales.firstOrNull()
            return bestLocale?.toLanguageTag()
        }
    actual val nativeName: String?
        get() {
            val locale = Locale(language.langCode)
            return locale.getDisplayLanguage(locale)
        }
    actual val displayNameInEnglish: String
        get() {
            if (language.englishName.isNotBlank()) {
                return language.englishName
            } else {
                val locale = Locale(language.langCode)
                return locale.getDisplayLanguage(Locale.ENGLISH)
            }
        }
    actual val flagEmoji: String
        get() = LanguageUtils.getFlagEmoji(language.langCode)

    actual companion object {
        actual fun fromLanguageCode(languageCode: String): UiLanguage {
            val language = Language(langCode = languageCode)
            return fromLanguage(language)
        }

        actual fun fromLanguage(language: Language): UiLanguage {
            return UiLanguage(
                language = language,
            )
        }
    }
}