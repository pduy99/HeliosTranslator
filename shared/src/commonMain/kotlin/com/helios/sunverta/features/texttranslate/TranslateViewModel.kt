package com.helios.sunverta.features.texttranslate

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
                    UiLanguage.fromLanguage(it)
                })
            }
        }
    }

    fun onEvent(event: TextTranslateEvent) {
        when (event) {
            is TextTranslateEvent.ChangeTranslationText -> {
                _state.update {
                    it.copy(
                        fromText = event.text
                    )
                }
            }

            is TextTranslateEvent.ChooseFromLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveFromLanguage(event.language.language)
                }
                _state.update {
                    it.copy(isChoosingFromLanguage = false)
                }
            }

            is TextTranslateEvent.ChooseToLanguage -> {
                viewModelScope.launch {
                    languageRepository.saveToLanguage(event.language.language)

                    _state.update {
                        it.copy(isChoosingToLanguage = false)
                    }

                    translate(state.value.copy(toLanguage = event.language))
                }

            }

            TextTranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }

            TextTranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(toText = null, isTranslating = false)
                    }
                }
            }

            TextTranslateEvent.OnErrorSeen -> {
                _state.update { it.copy(error = null) }
            }

            TextTranslateEvent.OpenFromLanguageDropDown -> {
                _state.update { it.copy(isChoosingFromLanguage = true) }
            }

            TextTranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true
                    )
                }
            }

            TextTranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false
                    )
                }
            }

            TextTranslateEvent.SwapLanguages -> {
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

            TextTranslateEvent.TextTranslate -> {
                translate(state.value)
            }

            is TextTranslateEvent.ReadAloudText -> {
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