package com.helios.kmptranslator.core.domain.usecase

import com.helios.kmptranslator.core.data.repository.TranslateHistoryRepository
import com.helios.kmptranslator.core.data.repository.TranslateRepository
import com.helios.kmptranslator.core.domain.mapper.toEntity
import com.helios.kmptranslator.core.domain.model.HistoryItem
import com.helios.kmptranslator.core.domain.model.Language
import com.helios.kmptranslator.core.domain.util.Result
import com.helios.kmptranslator.translate.TranslateError

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