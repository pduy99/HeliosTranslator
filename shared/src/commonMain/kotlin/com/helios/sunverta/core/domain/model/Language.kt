package com.helios.sunverta.core.domain.model

import com.helios.sunverta.core.network.deepl.dto.LanguageDto

data class Language(
    val langCode: String,
    val englishName: String = "",
)

fun LanguageDto.toExternalLanguage(): Language {
    return Language(
        langCode = this.language.lowercase(),
        englishName = this.englishName
    )
}