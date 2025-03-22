package com.helios.sunverta.core.data.repository

import com.helios.sunverta.core.data.datasource.LocalLanguageDataSource
import com.helios.sunverta.core.data.datasource.RemoteLanguageDataSource
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.domain.model.toExternalLanguage
import com.helios.sunverta.core.util.CommonFlow

class LanguageRepositoryImpl(
    private val remoteLanguageDataSource: RemoteLanguageDataSource,
    private val localLanguageDataSource: LocalLanguageDataSource
) : LanguageRepository {
    private var cachedAvailableLanguages: List<Language> = emptyList()

    override suspend fun getAvailableLanguages(): List<Language> {
        return if (cachedAvailableLanguages.isNotEmpty()) {
            cachedAvailableLanguages.toList()
        } else {
            remoteLanguageDataSource.getAvailableLanguages().map {
                it.toExternalLanguage()
            }.also {
                cachedAvailableLanguages = it
            }
        }
    }

    override fun getFromLanguage(): CommonFlow<Language> =
        localLanguageDataSource.getFromLanguage()

    override fun getToLanguage(): CommonFlow<Language> =
        localLanguageDataSource.getToLanguage()


    override suspend fun saveFromLanguage(language: Language) =
        localLanguageDataSource.saveFromLanguage(language)

    override suspend fun saveToLanguage(language: Language) =
        localLanguageDataSource.saveToLanguage(language)
}