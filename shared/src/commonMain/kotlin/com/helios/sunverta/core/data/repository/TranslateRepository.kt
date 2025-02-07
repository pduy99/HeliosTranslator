package com.helios.sunverta.core.data.repository

import com.helios.sunverta.core.domain.util.Result
import com.helios.sunverta.features.translate.TranslateError

interface TranslateRepository {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}