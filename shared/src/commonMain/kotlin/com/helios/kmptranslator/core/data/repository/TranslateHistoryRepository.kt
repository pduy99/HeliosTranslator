package com.helios.kmptranslator.core.data.repository

import com.helios.kmptranslator.core.util.CommonFlow
import database.HistoryEntity

interface TranslateHistoryRepository {

    fun getHistory(): CommonFlow<List<HistoryEntity>>

    suspend fun insertHistory(item: HistoryEntity)
}