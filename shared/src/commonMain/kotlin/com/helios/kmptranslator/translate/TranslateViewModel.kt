package com.helios.kmptranslator.translate

import com.helios.kmptranslator.core.domain.usecase.GetTranslateHistoryUseCase
import com.helios.kmptranslator.core.domain.usecase.TranslateUseCase
import com.helios.kmptranslator.core.domain.util.Result
import com.helios.kmptranslator.core.presentation.UiHistoryItem
import com.helios.kmptranslator.core.presentation.UiLanguage
import com.helios.kmptranslator.core.speech.TextToSpeech
import com.helios.kmptranslator.core.util.toCommonStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateUseCase: TranslateUseCase,
    private val textToSpeech: TextToSpeech,
    getTranslateHistoryUseCase: GetTranslateHistoryUseCase,
    coroutineScope: CoroutineScope?,
) {

    private val viewModelScope =
        coroutineScope ?: CoroutineScope(Dispatchers.Main) // in iOS we will use Dispatcher.Main

    private var translateJob: Job? = null

    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state,
        getTranslateHistoryUseCase.execute()
    ) { state, history ->
        if (state.history != history) {
            state.copy(history = history.map { item ->
                UiHistoryItem(
                    id = item.id ?: -1,
                    fromText = item.fromText,
                    toText = item.toText,
                    fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                    toLanguage = UiLanguage.byCode(item.toLanguageCode)
                )
            })
        } else state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TranslateState())
        .toCommonStateFlow()

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
                _state.update {
                    it.copy(isChoosingFromLanguage = false, fromLanguage = event.language)
                }
            }

            is TranslateEvent.ChooseToLanguage -> {
                _state.update {
                    it.copy(isChoosingToLanguage = false, toLanguage = event.language)
                }
                translate(_state.value)
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
                if (_state.value.toText != null) {
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

            is TranslateEvent.SelectHistoryTranslationItem -> {
                translateJob?.cancel()
                _state.update {
                    it.copy(
                        fromText = event.item.fromText,
                        toText = event.item.toText,
                        isTranslating = false,
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage
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
                _state.update {
                    it.copy(
                        toLanguage = it.fromLanguage,
                        fromLanguage = it.toLanguage,
                        fromText = it.toText ?: "",
                        toText = if (it.toText != null) it.fromText else null
                    )
                }
            }

            TranslateEvent.Translate -> {
                translate(_state.value)
            }

            is TranslateEvent.SubmitVoiceResult -> {
                _state.update {
                    it.copy(
                        fromText = event.voiceResult ?: it.fromText,
                        isTranslating = if (event.voiceResult != null) false else it.isTranslating,
                        toText = if (event.voiceResult != null) null else it.toText
                    )
                }
                translate(_state.value)
            }

            is TranslateEvent.ReadAloudText -> {
                _state.value.toText?.let {
                    textToSpeech.speak(
                        language = state.value.toLanguage.language.langCode,
                        text = it,
                        onComplete = {})
                }
            }
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