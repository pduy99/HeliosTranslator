package com.helios.kmptranslator.android.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.kmptranslator.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.kmptranslator.features.history.TranslateHistoryEvent
import com.helios.kmptranslator.features.history.TranslateHistoryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateHistoryViewModel @Inject constructor(
    private val getTranslateHistoryUseCase: GetTranslateHistoryUseCase
) : ViewModel() {

    private val viewModel by lazy {
        TranslateHistoryViewModel(
            getTranslateHistoryUseCase = getTranslateHistoryUseCase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateHistoryEvent) {
        viewModel.onEvent(event)
    }
}