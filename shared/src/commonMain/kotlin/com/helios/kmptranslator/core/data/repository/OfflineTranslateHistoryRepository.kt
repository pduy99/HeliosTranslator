package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.data.datasource.TranslateHistoryDataSource
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

    override suspend fun deleteHistory(item: HistoryEntity) {
        return translateHistoryDataSource.deleteHistory(item)
    }

    override suspend fun deleteAllHistory() {
        return translateHistoryDataSource.deleteAllHistory()
    }

    override suspend fun deleteHistoryOlderThan(timestamp: Long) {
        return translateHistoryDataSource.deleteHistoryOlderThan(timestamp)
    }

    override fun getHistoryNewerThan(timestamp: Long): CommonFlow<List<HistoryEntity>> {
        return translateHistoryDataSource.getHistoryNewerThan(timestamp)
    }
}