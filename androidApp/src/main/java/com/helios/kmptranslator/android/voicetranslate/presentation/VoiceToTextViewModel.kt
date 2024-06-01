package com.helios.kmptranslator.android.voicetranslate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.voicetotext.domain.VoiceToTextParser
import com.helios.kmptranslator.voicetotext.VoiceToTextEvent
import com.helios.kmptranslator.voicetotext.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    private val parser: VoiceToTextParser
) : ViewModel() {

    private val viewModel: VoiceToTextViewModel = VoiceToTextViewModel(parser, viewModelScope)

    val state = viewModel.state

    fun onEvent(event: VoiceToTextEvent) = viewModel.onEvent(event)
}