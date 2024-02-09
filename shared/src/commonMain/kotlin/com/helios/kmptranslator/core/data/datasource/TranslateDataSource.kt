package com.helios.kmptranslator.core.data.datasource

interface TranslateDataSource {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): String
}