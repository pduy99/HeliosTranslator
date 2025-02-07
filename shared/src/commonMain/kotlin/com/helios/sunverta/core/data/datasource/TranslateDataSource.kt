package com.helios.sunverta.core.data.datasource

import com.helios.sunverta.core.domain.util.Result
import com.helios.sunverta.features.translate.TranslateError

interface TranslateDataSource {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}