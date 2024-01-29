package com.helios.kmptranslator.voicetotext.domain

import com.helios.kmptranslator.core.util.CommonStateFlow

interface VoiceToTextParser {

    val state: CommonStateFlow<VoiceToTextParserState>

    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}