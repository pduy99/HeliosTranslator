package com.helios.sunverta.features.scantranslate

import com.helios.sunverta.core.domain.model.CommonImage
import com.helios.sunverta.core.presentation.UiLanguage

sealed class ScanTranslateEvent {

    data object CaptureImage : ScanTranslateEvent()

    data class TranslateImage(val image: CommonImage) : ScanTranslateEvent()

    data object ToggleTorch : ScanTranslateEvent()

    data object Reset : ScanTranslateEvent()

    data class ChooseFromLanguage(val language: UiLanguage) : ScanTranslateEvent()

    data class ChooseToLanguage(val language: UiLanguage) : ScanTranslateEvent()

    data object StopChoosingLanguage : ScanTranslateEvent()

    data object OpenFromLanguagePicker : ScanTranslateEvent()

    data object OpenToLanguagePicker : ScanTranslateEvent()

}