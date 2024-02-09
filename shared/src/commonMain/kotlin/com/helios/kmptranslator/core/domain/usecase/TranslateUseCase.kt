package com.helios.kmptranslator.core.domain.usecase

import com.helios.kmptranslator.core.data.repository.TranslateRepository
import com.helios.kmptranslator.core.domain.model.HistoryItem
import com.helios.kmptranslator.core.domain.model.Language
import com.helios.kmptranslator.core.util.Resource
import com.helios.kmptranslator.core.data.error.TranslateException
import com.helios.kmptranslator.core.data.repository.TranslateHistoryRepository
import com.helios.kmptranslator.core.domain.mapper.toEntity

class TranslateUseCase(
    private val translateRepository: TranslateRepository,
    private val translateHistoryRepository: TranslateHistoryRepository
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = translateRepository.translate(
                fromLanguage.langCode, toLanguage.langCode, fromText,
            )
            translateHistoryRepository.insertHistory(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = translatedText
                ).toEntity()
            )

            Resource.Success(translatedText)
        } catch (ex: TranslateException) {
            ex.printStackTrace()
            Resource.Error(ex)
        }
    }
}