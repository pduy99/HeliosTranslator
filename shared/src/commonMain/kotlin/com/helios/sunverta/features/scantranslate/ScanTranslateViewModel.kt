package com.helios.sunverta.features.scantranslate


import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.core.util.toCommonStateFlow
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanTranslateViewModel(
    val imageTranslator: ImageTranslator,
    languageRepository: LanguageRepository,
    coroutineScope: CoroutineScope?
) {

    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private val _state =
        MutableStateFlow(
            ScanTranslateUiState()
        )

    val state = combine(
        _state,
        languageRepository.getToLanguage(),
        languageRepository.getFromLanguage()
    ) { state, toLanguage, fromLanguage ->
        state.copy(
            toLanguage = UiLanguage.fromLanguageCode(toLanguage.langCode),
            fromLanguage = UiLanguage.fromLanguageCode(fromLanguage.langCode)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScanTranslateUiState())
        .toCommonStateFlow()

    fun onEvent(event: ScanTranslateEvent) {
        when (event) {
            is ScanTranslateEvent.TranslateImage -> {
                _state.update {
                    it.copy(capturedImage = event.image)
                }

                viewModelScope.launch {
                    _state.update {
                        it.copy(isTranslating = true)
                    }
                    val translatedTextBlock = imageTranslator.translateImage(
                        fromLanguage = _state.value.fromLanguage.language,
                        toLanguage = _state.value.toLanguage.language,
                        image = event.image
                    )
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            translatedTextBlock = translatedTextBlock
                        )
                    }
                }
            }

            ScanTranslateEvent.Reset -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        translatedTextBlock = emptyList(),
                        capturedImage = null
                    )
                }
            }

            is ScanTranslateEvent.ToggleTorch -> {
                _state.update {
                    it.copy(isTorchEnable = !it.isTorchEnable)
                }
            }

            else -> {

            }
        }
    }
}