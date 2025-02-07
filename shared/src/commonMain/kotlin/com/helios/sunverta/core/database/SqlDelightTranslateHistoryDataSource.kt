package com.helios.sunverta.core.database

import com.helios.sunverta.core.data.datasource.TranslateHistoryDataSource
import com.helios.sunverta.core.util.CommonFlow
import com.helios.sunverta.core.util.toCommonFlow
import com.helios.sunverta.database.TranslateDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.HistoryEntity
import kotlinx.datetime.Clock

class SqlDelightTranslateHistoryDataSource(
    db: TranslateDatabase
) : TranslateHistoryDataSource {

    private val queries = db.translateQueries

    override fun getHistory(): CommonFlow<List<HistoryEntity>> {
        return queries.getHistory()
            .asFlow()
            .mapToList()
            .toCommonFlow()
    }

    override suspend fun insertHistory(item: HistoryEntity) {
        queries.insertHistoryEntity(
            id = null, // always set to null since the id is auto increment
            fromLanguageCode = item.fromLanguageCode,
            fromText = item.fromText,
            toLanguageCode = item.toLanguageCode,
            toText = item.toText,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    override suspend fun deleteHistory(item: HistoryEntity) {
        queries.deleteHistory(item.id)
    }

    override suspend fun deleteAllHistory() {
        queries.deleteAllHistory()
    }

    override suspend fun deleteHistoryOlderThan(timestamp: Long) {
        queries.deleteHistoryOlderThan(timestamp)
    }

    override fun getHistoryNewerThan(timestamp: Long): CommonFlow<List<HistoryEntity>> {
        return queries.getHistoryNewerThan(timestamp).asFlow().mapToList().toCommonFlow()
    }
}