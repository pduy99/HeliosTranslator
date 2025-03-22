package com.helios.sunverta.core.database

import com.helios.sunverta.core.data.datasource.LocalLanguageDataSource
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.util.CommonFlow
import com.helios.sunverta.core.util.toCommonFlow
import com.helios.sunverta.database.TranslateDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.map

class LocalLanguageDataSourceImpl(
    db: TranslateDatabase
) : LocalLanguageDataSource {

    private val queries = db.languagePreferenceQueries

    override fun getFromLanguage(): CommonFlow<Language> {
        return queries.getFromLanguage().asFlow().mapToOneOrNull().map { languagePrefs ->
            if (languagePrefs == null) {
                return@map DEFAULT_FROM_LANGUAGE.also {
                    saveFromLanguage(it)
                }
            } else {
                Language(
                    langCode = languagePrefs.languageCode
                )
            }
        }.toCommonFlow()
    }

    override fun getToLanguage(): CommonFlow<Language> {
        return queries.getToLanguage().asFlow().mapToOneOrNull().map { languagePrefs ->
            if (languagePrefs == null) {
                return@map DEFAULT_TO_LANGUAGE.also {
                    saveToLanguage(it)
                }
            } else {
                Language(
                    langCode = languagePrefs.languageCode
                )
            }
        }.toCommonFlow()
    }

    override suspend fun saveFromLanguage(language: Language) {
        queries.insertOrReplaceFromLanguage(
            languageCode = language.langCode,
        )
    }

    override suspend fun saveToLanguage(language: Language) {
        queries.insertOrReplaceToLanguage(
            languageCode = language.langCode,
        )
    }

    companion object {
        val DEFAULT_FROM_LANGUAGE = Language("en")
        val DEFAULT_TO_LANGUAGE = Language("es")
    }
}