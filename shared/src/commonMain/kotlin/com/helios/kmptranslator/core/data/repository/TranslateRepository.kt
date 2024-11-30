package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.domain.util.Result
import com.helios.kmptranslator.features.translate.TranslateError

interface TranslateRepository {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}