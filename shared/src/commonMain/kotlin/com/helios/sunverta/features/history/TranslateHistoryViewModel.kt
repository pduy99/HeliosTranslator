package com.helios.sunverta.features.history

import com.helios.sunverta.core.data.repository.TranslateHistoryRepository
import com.helios.sunverta.core.domain.usecase.GetAndCleanTranslateHistoryUseCase
import com.helios.sunverta.core.presentation.UiHistoryItem
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.core.util.CommonStateFlow
import com.helios.sunverta.core.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslateHistoryViewModel(
    private val getAndCleanTranslateHistoryUseCase: GetAndCleanTranslateHistoryUseCase,
    private val translateHistoryRepository: TranslateHistoryRepository,
    coroutineScope: CoroutineScope?,
) {
    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private val _state = MutableStateFlow(TranslateHistoryUiState())
    val state: CommonStateFlow<TranslateHistoryUiState> = _state.asStateFlow().toCommonStateFlow()

    init {
        viewModelScope.launch {
            getAndCleanTranslateHistoryUseCase.execute().collect { historyItems ->
                _state.update {
                    it.copy(
                        history = historyItems.map { item ->
                            UiHistoryItem(
                                id = item.id ?: -1,
                                fromText = item.fromText,
                                toText = item.toText,
                                fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                                toLanguage = UiLanguage.byCode(item.toLanguageCode)
                            )
                        }
                    )
                }
            }
        }
    }

    fun onEvent(event: TranslateHistoryEvent) {
        when (event) {
            is TranslateHistoryEvent.DeleteAllHistory -> {
                viewModelScope.launch {
                    translateHistoryRepository.deleteAllHistory()
                }
            }

            is TranslateHistoryEvent.ToggleMenu -> {
                _state.update {
                    it.copy(menuExpanded = if (event.forceClose) false else !it.menuExpanded)
                }
            }
        }
    }
}