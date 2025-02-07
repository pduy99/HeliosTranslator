package com.helios.kmptranslator.features.scantranslate

import com.helios.kmptranslator.core.util.CommonStateFlow
import com.helios.kmptranslator.core.util.toCommonStateFlow
import com.helios.kmptranslator.features.scantranslate.domain.ImageTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanTranslateViewModel(
    val imageTranslator: ImageTranslator,
    coroutineScope: CoroutineScope?
) {

    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private val _state =
        MutableStateFlow(
            ScanTranslateUiState()
        )
    val state: CommonStateFlow<ScanTranslateUiState> = _state.asStateFlow().toCommonStateFlow()

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
                        it.copy(isTranslating = false, translatedTextBlock = translatedTextBlock)
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