package com.helios.sunverta.android.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.domain.usecase.GetAndCleanTranslateHistoryUseCase
import com.helios.sunverta.features.history.TranslateHistoryEvent
import com.helios.sunverta.features.history.TranslateHistoryViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateHistoryViewModel @Inject constructor(
    private val getAndCleanTranslateHistoryUseCase: GetAndCleanTranslateHistoryUseCase,
    private val translateHistoryRepository: TranslateHistoryRepository,
) : ViewModel() {

    private val viewModel by lazy {
        TranslateHistoryViewModel(
            getAndCleanTranslateHistoryUseCase = getAndCleanTranslateHistoryUseCase,
            translateHistoryRepository = translateHistoryRepository,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateHistoryEvent) {
        viewModel.onEvent(event)
    }
}