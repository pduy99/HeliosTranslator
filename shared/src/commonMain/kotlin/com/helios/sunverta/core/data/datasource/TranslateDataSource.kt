package com.helios.sunverta.core.data.datasource

import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.result.Result

interface TranslateDataSource {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}