package com.helios.sunverta.core.speech

expect class TextToSpeech {
    fun speak(language: String, text: String, onComplete: () -> Unit)
    fun stopSpeaking()
    fun shutdown()
}