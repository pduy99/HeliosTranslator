package com.helios.sunverta.core.network.deepl.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LanguageDto(
    val language: String,
    @SerialName("name")
    val englishName: String,
)