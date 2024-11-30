package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.util.CommonFlow
import database.HistoryEntity

interface TranslateHistoryRepository {

    fun getHistory(): CommonFlow<List<HistoryEntity>>

    suspend fun insertHistory(item: HistoryEntity)

    suspend fun deleteHistory(item: HistoryEntity)

    suspend fun deleteAllHistory()

    suspend fun deleteHistoryOlderThan(timestamp: Long)

    fun getHistoryNewerThan(timestamp: Long): CommonFlow<List<HistoryEntity>>
}