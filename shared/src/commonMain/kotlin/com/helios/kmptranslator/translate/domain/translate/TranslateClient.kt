package com.helios.kmptranslator.translate.domain.translate

import com.helios.kmptranslator.core.domain.language.Language

interface TranslateClient {

    suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String
}