package com.helios.kmptranslator.features.voicetotext.domain

data class VoiceToTextParserState(
    val result: String = "",
    val error: String? = null,
    val powerRatio: Float = 0f, // How loud the user is speaking
    val isSpeaking: Boolean = false,
)