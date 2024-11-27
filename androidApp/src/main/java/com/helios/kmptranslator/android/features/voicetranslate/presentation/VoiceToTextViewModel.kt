package com.helios.kmptranslator.android.features.voicetranslate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.voicetotext.ConversationTranslateEvent
import com.helios.kmptranslator.voicetotext.VoiceToTextViewModel
import com.helios.kmptranslator.voicetotext.domain.VoiceToTextParser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    private val parser: VoiceToTextParser,
    private val translateUseCase: TranslateUseCase
) : ViewModel() {

    private val viewModel: VoiceToTextViewModel =
        VoiceToTextViewModel(parser, translateUseCase, viewModelScope)

    val state = viewModel.state

    fun onEvent(event: ConversationTranslateEvent) = viewModel.onEvent(event)
}