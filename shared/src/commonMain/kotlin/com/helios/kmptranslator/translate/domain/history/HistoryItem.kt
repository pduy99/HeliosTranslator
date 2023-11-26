package com.helios.kmptranslator.translate.domain.history

import com.helios.kmptranslator.core.domain.language.Language

class HistoryItem(
    val id: Long?,
    val fromLanguageCode: String,
    val fromText: String,
    val toLanguageCode: String,
    val toText: String
)