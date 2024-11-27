package com.helios.kmptranslator.android.features.texttranslate.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.core.speech.TextToSpeech
import com.helios.kmptranslator.translate.TranslateEvent
import com.helios.kmptranslator.translate.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val getTranslateHistoryUseCase: GetTranslateHistoryUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val viewModel by lazy {
        TranslateViewModel(
            translateUseCase = translateUseCase,
            textToSpeech = TextToSpeech(context),
            getTranslateHistoryUseCase = getTranslateHistoryUseCase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event)
    }

    override fun onCleared() {
        super.onCleared()
        viewModel.shutdownTts()
    }
}