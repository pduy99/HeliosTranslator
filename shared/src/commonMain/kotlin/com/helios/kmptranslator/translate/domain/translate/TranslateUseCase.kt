package com.helios.kmptranslator.translate.domain.translate

import com.helios.kmptranslator.core.domain.language.Language
import com.helios.kmptranslator.core.domain.util.Resource
import com.helios.kmptranslator.translate.domain.history.HistoryDataSource
import com.helios.kmptranslator.translate.domain.history.HistoryItem

class TranslateUseCase(
    private val client: TranslateClient,
    private val historyDataSource: HistoryDataSource
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = client.translate(
                fromLanguage, fromText, toLanguage
            )
            historyDataSource.insertHistory(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = translatedText
                )
            )

            Resource.Success(translatedText)
        } catch (ex: TranslateException) {
            ex.printStackTrace()
            Resource.Error(ex)
        }
    }
}