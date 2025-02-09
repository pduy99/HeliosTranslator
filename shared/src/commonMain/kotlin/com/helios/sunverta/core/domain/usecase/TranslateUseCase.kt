package com.helios.sunverta.core.domain.usecase

import com.helios.sunverta.core.data.model.TranslateError
import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.data.repository.TranslateRepository
import com.helios.sunverta.core.domain.mapper.toEntity
import com.helios.sunverta.core.domain.model.HistoryItem
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.result.Result

class TranslateUseCase(
    private val translateRepository: TranslateRepository,
    private val translateHistoryRepository: TranslateHistoryRepository
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Result<String, TranslateError> {
        val translateResult = translateRepository.translate(
            fromLanguage.langCode, toLanguage.langCode, fromText,
        )

        if (translateResult is Result.Success) {
            saveHistory(fromText, translateResult.data, fromLanguage, toLanguage)
        }

        return translateResult
    }

    private suspend fun saveHistory(
        fromText: String,
        translatedText: String,
        fromLanguage: Language,
        toLanguage: Language
    ) {
        translateHistoryRepository.insertHistory(
            HistoryItem(
                id = null,
                fromLanguageCode = fromLanguage.langCode,
                fromText = fromText,
                toLanguageCode = toLanguage.langCode,
                toText = translatedText
            ).toEntity()
        )
    }
}