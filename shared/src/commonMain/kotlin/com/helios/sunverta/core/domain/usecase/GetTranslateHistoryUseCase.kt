package com.helios.sunverta.core.domain.usecase

import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.domain.mapper.toHistoryItem
import com.helios.sunverta.core.domain.model.HistoryItem
import com.helios.sunverta.core.util.CommonFlow
import com.helios.sunverta.core.util.toCommonFlow
import kotlinx.coroutines.flow.map

class GetTranslateHistoryUseCase(
    private val translateHistoryRepository: TranslateHistoryRepository
) {

    fun execute(): CommonFlow<List<HistoryItem>> {
        return translateHistoryRepository.getHistory().map {
            it.map { historyEntity ->
                historyEntity.toHistoryItem()
            }
        }.toCommonFlow()
    }
}