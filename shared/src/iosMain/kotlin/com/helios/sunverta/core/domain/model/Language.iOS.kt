package com.helios.sunverta.core.domain.model

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

actual fun getValidBcp47Code(languageCode: String): String? {
    return languageCode
}

actual fun getDisplayNameInEnglish(languageCode: String): String? {
    val englishLocale = NSLocale("en_US")
    return englishLocale.localizedStringForLanguageCode(languageCode)
}

actual fun getNativeLanguageName(languageCode: String): String? {
    return NSLocale.currentLocale.localizedStringForLanguageCode(languageCode)
}