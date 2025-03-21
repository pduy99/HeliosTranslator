package com.helios.sunverta.core.domain.model

import com.helios.sunverta.core.network.dto.LanguageDto

data class Language(
    val langCode: String,
)

fun LanguageDto.toExternalLanguage(): Language {
    return Language(
        langCode = this.code,
    )
}