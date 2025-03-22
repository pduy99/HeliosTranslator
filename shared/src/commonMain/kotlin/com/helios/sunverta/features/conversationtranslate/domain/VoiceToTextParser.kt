package com.helios.sunverta.features.conversationtranslate.domain

import com.helios.sunverta.core.util.CommonStateFlow

interface VoiceToTextParser {

    val state: CommonStateFlow<VoiceToTextParserState>

    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}