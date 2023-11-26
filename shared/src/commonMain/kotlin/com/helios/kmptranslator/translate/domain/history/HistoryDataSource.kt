package com.helios.kmptranslator.translate.domain.history

import com.helios.kmptranslator.core.domain.util.CommonFlow

interface HistoryDataSource {
    fun getHistory(): CommonFlow<List<HistoryItem>>

    suspend fun insertHistory(item: HistoryItem)
}