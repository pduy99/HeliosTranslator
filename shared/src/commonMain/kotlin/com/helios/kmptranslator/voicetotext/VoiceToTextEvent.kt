package com.helios.kmptranslator.voicetotext

sealed class VoiceToTextEvent {

    data class PermissionResult(
        val isGranted: Boolean,
        val isPermanentlyDenied: Boolean
    ) : VoiceToTextEvent()

    data class ToggleRecording(val languageCode: String) : VoiceToTextEvent()
    data object Reset : VoiceToTextEvent()
}