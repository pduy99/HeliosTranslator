package com.helios.kmptranslator.voicetotext

import com.helios.kmptranslator.voicetotext.domain.VoiceToTextParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val parser: VoiceToTextParser,
    coroutineScope: CoroutineScope? = null
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(VoiceToTextState())
    val state = _state.combine(parser.state) { state, voiceResult ->
        state.copy(
            spokenText = voiceResult.result,
            recordError = voiceResult.error,
            displayState = when {
                voiceResult.isSpeaking -> DisplayState.SPEAKING
                voiceResult.error != null -> DisplayState.ERROR
                voiceResult.result.isNotBlank() && !voiceResult.isSpeaking -> DisplayState.DISPLAYING_RESULTS
                else -> DisplayState.WAITING_TO_TALK
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VoiceToTextState())

    init {
        viewModelScope.launch {
            while (true) {
                if (state.value.displayState == DisplayState.SPEAKING) {
                    _state.update {
                        it.copy(powerRatios = it.powerRatios + parser.state.value.powerRatio)
                    }
                }
                delay(50L)
            }
        }
    }

    private fun toggleRecording(languageCode: String) {
        parser.cancel()
        if (state.value.displayState == DisplayState.SPEAKING) {
            parser.stopListening()
        } else {
            parser.startListening(languageCode)
        }
    }

    fun onEvent(event: VoiceToTextEvent) = when (event) {
        is VoiceToTextEvent.PermissionResult -> {
            _state.update { it.copy(canRecord = event.isGranted) }
        }

        is VoiceToTextEvent.ToggleRecording -> toggleRecording(event.languageCode)
        is VoiceToTextEvent.Reset -> {
            parser.reset()
            _state.update { VoiceToTextState() }
        }

        else -> Unit
    }
}