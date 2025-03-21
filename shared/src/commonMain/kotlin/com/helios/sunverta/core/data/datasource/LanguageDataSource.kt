package com.helios.sunverta.core.data.datasource

import com.helios.sunverta.core.network.deepl.dto.LanguageDto

interface RemoteLanguageDataSource {
    suspend fun getAvailableLanguages(): List<LanguageDto>
}