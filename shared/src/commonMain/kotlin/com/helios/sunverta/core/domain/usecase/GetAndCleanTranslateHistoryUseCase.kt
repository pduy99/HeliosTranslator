package com.helios.sunverta.core.domain.usecase

import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.domain.mapper.toHistoryItem
import com.helios.sunverta.core.domain.model.DeletionPeriod
import com.helios.sunverta.core.domain.model.HistoryItem
import com.helios.sunverta.core.util.CommonFlow
import com.helios.sunverta.core.util.toCommonFlow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class GetAndCleanTranslateHistoryUseCase(
    private val translateHistoryRepository: TranslateHistoryRepository,
    private val deletionPeriod: DeletionPeriod = DeletionPeriod.ONE_DAY // Hardcode this until we have a setting screen
) {

    fun execute(): CommonFlow<List<HistoryItem>> = flow {
        val cutoffTimestamp = calculateCutoffTimestamp(deletionPeriod)

        // Delete old items
        translateHistoryRepository.deleteHistoryOlderThan(cutoffTimestamp)

        // Emit remaining items
        translateHistoryRepository.getHistoryNewerThan(cutoffTimestamp).collect { items ->
            emit(items.map {
                it.toHistoryItem()
            })
        }
    }.toCommonFlow()

    private fun calculateCutoffTimestamp(deletionPeriod: DeletionPeriod): Long {
        return when (deletionPeriod) {
            DeletionPeriod.NEVER -> 0L
            else -> {
                val currentTime = Clock.System.now().toEpochMilliseconds()
                currentTime - (deletionPeriod.hours * 3600 * 1000)
            }
        }
    }
}