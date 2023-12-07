package com.helios.kmptranslator.android.core.navigation

sealed class Routes(val name: String) {
    data object Translate: Routes("translate")
    data object VoiceToText: Routes("voice_to_text")
}