package com.helios.kmptranslator.core.database

import com.helios.kmptranslator.core.data.datasource.TranslateHistoryDataSource
import com.helios.kmptranslator.core.util.CommonFlow
import com.helios.kmptranslator.core.util.toCommonFlow
import com.helios.kmptranslator.database.TranslateDatabase
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
}