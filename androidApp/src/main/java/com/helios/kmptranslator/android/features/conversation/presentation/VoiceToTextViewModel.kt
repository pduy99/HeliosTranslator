package com.helios.kmptranslator.android.features.conversation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.features.voicetotext.ConversationTranslateEvent
import com.helios.kmptranslator.features.voicetotext.VoiceToTextViewModel
import com.helios.kmptranslator.features.voicetotext.domain.VoiceToTextParser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    parser: VoiceToTextParser,
    translateUseCase: TranslateUseCase
) : ViewModel() {

    private val viewModel: VoiceToTextViewModel =
        VoiceToTextViewModel(parser, translateUseCase, viewModelScope)

    val state = viewModel.state

    fun onEvent(event: ConversationTranslateEvent) = viewModel.onEvent(event)
}