package com.helios.sunverta.features.voicetotext

import co.touchlab.kermit.Logger
import com.helios.sunverta.core.data.repository.LanguageRepository
import com.helios.sunverta.core.domain.model.Language
import com.helios.sunverta.core.domain.usecase.TranslateUseCase
import com.helios.sunverta.core.presentation.UiLanguage
import com.helios.sunverta.core.result.Result
import com.helios.sunverta.features.voicetotext.domain.VoiceToTextParser
import com.helios.sunverta.features.voicetotext.domain.VoiceToTextParserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val parser: VoiceToTextParser,
    private val translateUseCase: TranslateUseCase,
    private val languageRepository: LanguageRepository,
    coroutineScope: CoroutineScope? = null
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private var translateJob: Job? = null

    private val _state = MutableStateFlow(ConversationTranslateUiState())
    val state = combine(
        _state,
        parser.state,
        languageRepository.getToLanguage(),
        languageRepository.getFromLanguage(),
        ::mergeStates
    )
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            ConversationTranslateUiState()
        )

    init {
        viewModelScope.launch {
            parser.state.collect { parserState ->
                if (!parserState.isSpeaking) {
                    Logger.d(tag = "DUY", messageString = "TranslateIfNeeded")
                    translateIfNeeded()
                }
            }
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    availableLanguages = languageRepository.getAvailableLanguages().map {
                        UiLanguage.fromLanguageCode(it.langCode)
                    }
                )
            }
        }
    }

    private fun mergeStates(
        uiState: ConversationTranslateUiState,
        parserState: VoiceToTextParserState,
        toLanguage: Language,
        fromLanguage: Language
    ): ConversationTranslateUiState {
        return uiState.copy(
            recordError = parserState.error,
            isListening = parserState.isSpeaking,
            powerRatio = parserState.powerRatio,
            personOne = uiState.personOne.copy(
                language = UiLanguage.fromLanguageCode(fromLanguage.langCode),
                text = if (uiState.personTalking == ConversationTranslateUiState.TalkingPerson.PERSON_ONE)
                    parserState.result else uiState.personOne.text
            ),
            personTwo = uiState.personTwo.copy(
                language = UiLanguage.fromLanguageCode(toLanguage.langCode),
                text = if (uiState.personTalking == ConversationTranslateUiState.TalkingPerson.PERSON_TWO)
                    parserState.result else uiState.personTwo.text
            ),
            personTalking = if (parserState.isSpeaking) uiState.personTalking else ConversationTranslateUiState.TalkingPerson.NONE
        )
    }

    fun onEvent(event: ConversationTranslateEvent) {
        viewModelScope.launch {
            when (event) {
                is ConversationTranslateEvent.PermissionResult -> updatePermission(event.isGranted)
                is ConversationTranslateEvent.ChooseLanguage -> updateLanguage(
                    event.person,
                    event.language
                )

                is ConversationTranslateEvent.ToggleRecording -> toggleRecording(event.person)
                is ConversationTranslateEvent.OpenLanguageDropDown -> handleOpenLanguageDropDown(
                    event.person
                )

                is ConversationTranslateEvent.StopChoosingLanguage -> handleStopChoosingLanguage()

                is ConversationTranslateEvent.ToggleFaceToFaceMode -> {
                    _state.update {
                        it.copy(faceToFaceMode = !it.faceToFaceMode)
                    }
                }
            }
        }
    }

    private fun handleOpenLanguageDropDown(person: ConversationTranslateUiState.TalkingPerson) {
        _state.update {
            when (person) {
                ConversationTranslateUiState.TalkingPerson.PERSON_ONE ->
                    it.copy(personOne = it.personOne.copy(isChoosingLanguage = true))

                ConversationTranslateUiState.TalkingPerson.PERSON_TWO ->
                    it.copy(personTwo = it.personTwo.copy(isChoosingLanguage = true))

                else -> it
            }
        }
    }

    private fun handleStopChoosingLanguage() {
        _state.update {
            it.copy(
                personOne = it.personOne.copy(isChoosingLanguage = false),
                personTwo = it.personTwo.copy(isChoosingLanguage = false)
            )
        }
    }

    private fun updatePermission(isGranted: Boolean) {
        _state.update { it.copy(canRecord = isGranted) }
    }

    private fun updateLanguage(
        person: ConversationTranslateUiState.TalkingPerson,
        language: UiLanguage
    ) {
        _state.update {
            when (person) {
                ConversationTranslateUiState.TalkingPerson.PERSON_ONE -> {
                    viewModelScope.launch {
                        languageRepository.saveFromLanguage(language.language)
                    }
                    it.copy(
                        personOne = it.personOne.copy(
                            isChoosingLanguage = false,
                        )
                    )
                }


                ConversationTranslateUiState.TalkingPerson.PERSON_TWO -> {
                    viewModelScope.launch {
                        languageRepository.saveToLanguage(language.language)
                    }
                    it.copy(
                        personTwo = it.personTwo.copy(
                            isChoosingLanguage = false,
                        )
                    )
                }

                else -> it
            }
        }
    }

    private fun toggleRecording(person: ConversationTranslateUiState.TalkingPerson) {
        val currentState = state.value
        if (currentState.isListening) {
            parser.stopListening()
        } else {
            _state.update {
                it.copy(
                    personTalking = person,
                    personOne = it.personOne.copy(text = ""),
                    personTwo = it.personTwo.copy(text = "")
                )
            }
            val languageCode = currentState.getCurrentLanguage(person).language.bcp47Code
                ?: currentState.getCurrentLanguage(person).language.langCode
            parser.startListening(languageCode)
        }
    }

    private fun translateIfNeeded() {
        val state = state.value
        val parserState = parser.state.value

        if (state.isTranslating) {
            return
        }

        val fromLanguage =
            if (state.personTalking == ConversationTranslateUiState.TalkingPerson.PERSON_ONE) state.personOne.language.language else state.personTwo.language.language
        val toLanguage =
            if (state.personTalking == ConversationTranslateUiState.TalkingPerson.PERSON_ONE) state.personTwo.language.language else state.personOne.language.language
        val fromText = parserState.result

        if (fromText.isBlank()) {
            return
        }

        translateJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isTranslating = true,
                )
            }

            val result = translateUseCase.execute(
                fromLanguage = fromLanguage,
                fromText = fromText,
                toLanguage = toLanguage
            )

            when (result) {
                is Result.Success -> {
                    _state.update {
                        if (state.personTalking == ConversationTranslateUiState.TalkingPerson.PERSON_ONE) {
                            it.copy(
                                personTwo = it.personTwo.copy(text = result.data),
                                personOne = it.personOne.copy(text = fromText),
                                isTranslating = false,
                            )
                        } else {
                            it.copy(
                                personOne = it.personOne.copy(text = result.data),
                                personTwo = it.personTwo.copy(text = fromText),
                                isTranslating = false,
                            )
                        }
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
}