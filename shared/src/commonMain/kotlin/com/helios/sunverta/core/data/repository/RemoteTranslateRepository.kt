package com.helios.sunverta.core.data.repository

import com.helios.sunverta.core.data.datasource.TranslateDataSource
import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.result.Result

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