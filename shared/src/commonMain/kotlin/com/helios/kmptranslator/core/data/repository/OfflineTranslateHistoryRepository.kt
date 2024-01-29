package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.data.datasource.TranslateHistoryDataSource
import com.helios.kmptranslator.core.database.SqlDelightTranslateHistoryDataSource
import com.helios.kmptranslator.core.util.CommonFlow
import database.HistoryEntity

class OfflineTranslateHistoryRepository(
    private val translateHistoryDataSource: TranslateHistoryDataSource
) : TranslateHistoryRepository {
    override fun getHistory(): CommonFlow<List<HistoryEntity>> {
        return translateHistoryDataSource.getHistory()
    }

    override suspend fun insertHistory(item: HistoryEntity) {
        return translateHistoryDataSource.insertHistory(item)
    }
}