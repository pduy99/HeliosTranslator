package com.helios.sunverta.core.domain.mapper

import com.helios.sunverta.core.domain.model.HistoryItem
import database.HistoryEntity
import kotlinx.datetime.Clock

fun HistoryEntity.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id = id,
        fromLanguageCode = fromLanguageCode,
        fromText = fromText,
        toLanguageCode = toLanguageCode,
        toText = toText
    )
}

fun HistoryItem.toEntity(): HistoryEntity {
    return HistoryEntity(
        id = id ?: -1,
        fromLanguageCode = fromLanguageCode,
        fromText = fromText,
        toLanguageCode = toLanguageCode,
        toText = toText,
        timestamp = Clock.System.now().toEpochMilliseconds()
    )
}