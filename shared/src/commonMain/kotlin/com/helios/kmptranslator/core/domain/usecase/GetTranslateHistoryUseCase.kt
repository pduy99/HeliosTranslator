package com.helios.kmptranslator.core.domain.usecase

import com.helios.kmptranslator.core.data.repository.TranslateHistoryRepository
import com.helios.kmptranslator.core.domain.mapper.toHistoryItem
import com.helios.kmptranslator.core.domain.model.HistoryItem
import com.helios.kmptranslator.core.util.CommonFlow
import com.helios.kmptranslator.core.util.toCommonFlow
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