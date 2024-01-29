package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.data.datasource.TranslateDataSource


class RemoteTranslateRepository(
    private val translateDataSource: TranslateDataSource
) : TranslateRepository {

    override suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): String {
        return translateDataSource.translate(fromLanguageCode, toLanguageCode, fromText)
    }
}