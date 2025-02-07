package com.helios.sunverta.android.features.text.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.sunverta.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.speech.TextToSpeech
import com.helios.sunverta.features.translate.TranslateEvent
import com.helios.sunverta.features.translate.TranslateViewModel
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