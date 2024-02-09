package com.helios.kmptranslator.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TranslatedDto(
    val translatedText: String
)