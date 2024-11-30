package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.data.datasource.TranslateDataSource
import com.helios.kmptranslator.core.domain.util.Result
import com.helios.kmptranslator.features.translate.TranslateError

class RemoteTranslateRepository(
    private val translateDataSource: TranslateDataSource
) : TranslateRepository {

    override suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError> {
        return translateDataSource.translate(fromLanguageCode, toLanguageCode, fromText)
    }
}