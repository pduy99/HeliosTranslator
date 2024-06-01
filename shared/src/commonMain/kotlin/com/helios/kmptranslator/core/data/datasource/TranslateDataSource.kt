package com.helios.kmptranslator.core.data.datasource

import com.helios.kmptranslator.core.domain.util.Result
import com.helios.kmptranslator.translate.TranslateError

interface TranslateDataSource {

    suspend fun translate(
        fromLanguageCode: String,
        toLanguageCode: String,
        fromText: String
    ): Result<String, TranslateError>
}