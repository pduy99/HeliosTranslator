package com.helios.sunverta.core.domain.model

import com.helios.sunverta.core.network.dto.LanguageDto

data class Language(
    val langCode: String,
) {
    val bcp47Code: String?
        get() {
            return getValidBcp47Code(langCode)
        }

    val nativeName: String?
        get() {
            return getNativeLanguageName(langCode)
        }

    val displayNameInEnglish: String?
        get() {
            return getDisplayNameInEnglish(langCode)
        }
}

fun LanguageDto.toExternalLanguage(): Language {
    return Language(
        langCode = this.code,
    )
}

expect fun getValidBcp47Code(languageCode: String): String?

expect fun getNativeLanguageName(languageCode: String): String?

expect fun getDisplayNameInEnglish(languageCode: String): String?