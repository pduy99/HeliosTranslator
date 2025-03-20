package com.helios.sunverta.android.features.conversation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.features.voicetotext.ConversationTranslateEvent
import com.helios.sunverta.features.voicetotext.ConversationTranslateViewModel
import com.helios.sunverta.features.voicetotext.domain.VoiceToTextParser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    parser: VoiceToTextParser,
    translateUseCase: TranslateUseCase,
    languageRepository: LanguageRepository,
) : ViewModel() {

    private val viewModel: ConversationTranslateViewModel =
        ConversationTranslateViewModel(parser, translateUseCase, languageRepository, viewModelScope)

    val state = viewModel.state

    fun onEvent(event: ConversationTranslateEvent) = viewModel.onEvent(event)
}