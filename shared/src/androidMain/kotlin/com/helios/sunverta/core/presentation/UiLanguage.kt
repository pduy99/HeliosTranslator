package com.helios.sunverta.core.presentation

import androidx.annotation.DrawableRes
import com.helios.sunverta.R
import com.helios.sunverta.core.domain.model.Language
import java.util.Locale

actual class UiLanguage(
    @DrawableRes val drawableRes: Int,
    actual val language: Language,
) {
    actual val bcp47Code: String?
        get() {
            val locales = Locale.getAvailableLocales().filter { it.language == language.langCode }
            val bestLocale = locales.firstOrNull { it.country.isNotEmpty() } ?: locales.firstOrNull()
            return bestLocale?.toLanguageTag()
        }
    actual val nativeName: String?
        get() {
            val locale = Locale(language.langCode)
            return locale.getDisplayLanguage(locale)
        }
    actual val displayNameInEnglish: String?
        get() {
            val locale = Locale(language.langCode)
            return locale.getDisplayLanguage(Locale.ENGLISH)
        }

    actual companion object {
        actual fun fromLanguageCode(languageCode: String): UiLanguage {
            val language = Language(langCode = languageCode)
            return fromLanguage(language)
        }

        actual fun fromLanguage(language: Language): UiLanguage {
            return UiLanguage(
                language = language,
                drawableRes = when (language.langCode) {
                    "en" -> R.drawable.english
                    "ar" -> R.drawable.arabic
                    "sq" -> R.drawable.albanian
                    "bg" -> R.drawable.bulgaria
                    "zt" -> R.drawable.taiwan
                    "et" -> R.drawable.estonian
                    "ms" -> R.drawable.malay
                    "nb" -> R.drawable.norwegian
                    "th" -> R.drawable.thai
                    "az" -> R.drawable.azerbaijani
                    "zh" -> R.drawable.chinese
                    "cs" -> R.drawable.czech
                    "da" -> R.drawable.danish
                    "nl" -> R.drawable.dutch
                    "fi" -> R.drawable.finnish
                    "fr" -> R.drawable.french
                    "de" -> R.drawable.german
                    "el" -> R.drawable.greek
                    "he" -> R.drawable.hebrew
                    "hi" -> R.drawable.hindi
                    "hu" -> R.drawable.hungarian
                    "id" -> R.drawable.indonesian
                    "ga" -> R.drawable.irish
                    "it" -> R.drawable.italian
                    "ja" -> R.drawable.japanese
                    "ko" -> R.drawable.korean
                    "fa" -> R.drawable.persian
                    "pl" -> R.drawable.polish
                    "pt" -> R.drawable.portuguese
                    "ru" -> R.drawable.russian
                    "sk" -> R.drawable.slovak
                    "es" -> R.drawable.spanish
                    "sv" -> R.drawable.swedish
                    "tr" -> R.drawable.turkish
                    "uk" -> R.drawable.ukrainian
                    else -> {
                        R.drawable.english
                    }
                }
            )
        }
    }
}