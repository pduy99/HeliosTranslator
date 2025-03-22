package com.helios.sunverta.core.data.repository

import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.result.Result

interface TranslateRepository {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}