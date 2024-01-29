package com.helios.kmptranslator.core.data.repository

interface TranslateRepository {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): String
}