package com.helios.sunverta.features.translate

import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.core.result.Result
import com.helios.sunverta.core.speech.TextToSpeech
import com.helios.sunverta.core.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val textToSpeech: TextToSpeech,
    private val languageRepository: LanguageRepository,
    coroutineScope: CoroutineScope?,
) {

    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private var translateJob: Job? = null

    private val _state = MutableStateFlow(TranslateState())
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
                    UiLanguage.fromLanguageCode(it.langCode)
                })
            }
        }
    }

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update {
                    it.copy(
                        fromText = event.text
                    )
                }
            }

            is TranslateEvent.ChooseFromLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveFromLanguage(event.language.language)
                }
                _state.update {
                    it.copy(isChoosingFromLanguage = false)
                }
            }

            is TranslateEvent.ChooseToLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveToLanguage(event.language.language)

                    _state.update {
                        it.copy(isChoosingToLanguage = false)
                    }

                    translate(state.value.copy(toLanguage = event.language))
                }

            }

            TranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }

            TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(toText = null, isTranslating = false)
                    }
                }
            }

            TranslateEvent.OnErrorSeen -> {
                _state.update { it.copy(error = null) }
            }

            TranslateEvent.OpenFromLanguageDropDown -> {
                _state.update { it.copy(isChoosingFromLanguage = true) }
            }

            TranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true
                    )
                }
            }

            TranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false
                    )
                }
            }

            TranslateEvent.SwapLanguages -> {
                val oldFromLanguage = state.value.fromLanguage.language
                val oldToLanguage = state.value.toLanguage.language

                viewModelScope.launch {
                    languageRepository.saveToLanguage(oldFromLanguage)
                    languageRepository.saveFromLanguage(oldToLanguage)
                }
                _state.update {
                    it.copy(
                        fromText = it.toText ?: "",
                        toText = if (it.toText != null) it.fromText else null
                    )
                }
            }

            TranslateEvent.Translate -> {
                translate(state.value)
            }

            is TranslateEvent.ReadAloudText -> {
                state.value.toText?.let {
                    textToSpeech.speak(
                        language = state.value.toLanguage.language.langCode,
                        text = it,
                        onComplete = {})
                }
            }

            else -> Unit
        }
    }

    private fun translate(state: TranslateState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }
        translateJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isTranslating = true
                )
            }
            val result = translateUseCase.execute(
                fromLanguage = state.fromLanguage.language,
                fromText = state.fromText,
                toLanguage = state.toLanguage.language
            )
            when (result) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            toText = result.data
                        )
                    }
                }

                is Result.Failure -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            error = result.error
                        )
                    }
                }
            }
        }
    }

    fun shutdownTts() {
        textToSpeech.stopSpeaking()
        textToSpeech.shutdown()
    }
}