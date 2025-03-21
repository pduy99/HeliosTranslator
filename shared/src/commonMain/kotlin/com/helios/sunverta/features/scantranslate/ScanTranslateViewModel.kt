package com.helios.sunverta.features.scantranslate


import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.core.util.toCommonStateFlow
import com.helios.sunverta.features.scantranslate.domain.ImageTranslator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScanTranslateViewModel(
    private val imageTranslator: ImageTranslator,
    private val languageRepository: LanguageRepository,
    coroutineScope: CoroutineScope?
) {

    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private val _state =
        MutableStateFlow(
            ScanTranslateUiState()
        )

    val state = _state.toCommonStateFlow()

    init {
        viewModelScope.launch {
            combine(
                languageRepository.getToLanguage(),
                languageRepository.getFromLanguage()
            ) { toLanguage, fromLanguage ->
                _state.update {
                    it.copy(
                        fromLanguage = UiLanguage.fromLanguage(fromLanguage),
                        toLanguage = UiLanguage.fromLanguage(toLanguage)
                    )
                }
            }.collect()
        }

        viewModelScope.launch {
            _state.update {
                it.copy(availableLanguages = languageRepository.getAvailableLanguages().map {
                    UiLanguage.fromLanguage(it)
                })
            }
        }
    }

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

            is ScanTranslateEvent.ChooseFromLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveFromLanguage(event.language.language)
                }
                _state.update {
                    it.copy(isChoosingFromLanguage = false)
                }
            }

            is ScanTranslateEvent.ChooseToLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveToLanguage(event.language.language)

                    _state.update {
                        it.copy(isChoosingToLanguage = false)
                    }
                }
            }

            ScanTranslateEvent.OpenFromLanguagePicker -> {
                _state.update { it.copy(isChoosingFromLanguage = true) }
            }

            ScanTranslateEvent.OpenToLanguagePicker -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true
                    )
                }
            }

            ScanTranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false
                    )
                }
            }

            else -> {
                // Platform specific ViewModel handles
            }
        }
    }
}