package com.helios.kmptranslator.core.data.datasource

import com.helios.kmptranslator.core.util.CommonFlow
import database.HistoryEntity

interface TranslateHistoryDataSource {
    fun getHistory(): CommonFlow<List<HistoryEntity>>

    suspend fun insertHistory(item: HistoryEntity)
}