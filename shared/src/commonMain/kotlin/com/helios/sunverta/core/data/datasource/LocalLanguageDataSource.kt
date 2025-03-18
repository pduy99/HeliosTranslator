package com.helios.sunverta.core.data.datasource

import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.util.CommonFlow

interface LocalLanguageDataSource {
    fun getFromLanguage(): CommonFlow<Language>
    fun getToLanguage(): CommonFlow<Language>
    suspend fun saveFromLanguage(language: Language)
    suspend fun saveToLanguage(language: Language)
}