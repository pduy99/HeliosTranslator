package com.helios.kmptranslator.android.texttranslate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.translate.TranslateEvent
import com.helios.kmptranslator.translate.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val getTranslateHistoryUseCase: GetTranslateHistoryUseCase
) : ViewModel() {

    private val viewModel by lazy {
        TranslateViewModel(
            translateUseCase = translateUseCase,
            getTranslateHistoryUseCase = getTranslateHistoryUseCase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event)
    }
}