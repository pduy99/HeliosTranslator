package com.helios.sunverta.core.data.datasource

import com.helios.sunverta.core.network.dto.LanguageDto

interface RemoteLanguageDataSource {
    suspend fun getAvailableLanguages(): List<LanguageDto>
}