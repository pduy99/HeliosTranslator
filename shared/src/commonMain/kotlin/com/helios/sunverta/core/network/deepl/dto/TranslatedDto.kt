package com.helios.sunverta.core.network.deepl.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslatedDto(
    val translations: List<TranslatedItemDto>
)

@Serializable
data class TranslatedItemDto(
    @SerialName("detected_source_language")
    val detectedSourceLanguage: String,
    val text: String,
)