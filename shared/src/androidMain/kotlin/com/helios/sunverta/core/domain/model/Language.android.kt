package com.helios.sunverta.core.domain.model

import java.util.Locale

actual fun getValidBcp47Code(languageCode: String): String? {
    val locales = Locale.getAvailableLocales().filter { it.language == languageCode }
    val bestLocale = locales.firstOrNull { it.country.isNotEmpty() } ?: locales.firstOrNull()
    return bestLocale?.toLanguageTag()
}

actual fun getNativeLanguageName(languageCode: String): String? {
    val locale = Locale(languageCode)
    return locale.getDisplayLanguage(locale)
}

actual fun getDisplayNameInEnglish(languageCode: String): String? {
    val locale = Locale(languageCode)
    return locale.getDisplayLanguage(Locale.ENGLISH)
}